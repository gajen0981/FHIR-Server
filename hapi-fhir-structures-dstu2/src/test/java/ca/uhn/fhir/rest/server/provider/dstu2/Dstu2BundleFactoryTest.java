package ca.uhn.fhir.rest.server.provider.dstu2;

import ca.uhn.fhir.rest.server.BundleInclusionRule;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.dstu2.resource.Bundle;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class Dstu2BundleFactoryTest {
    private static FhirContext ourCtx;
    private List<IResource> myResourceList;
    private Dstu2BundleFactory myBundleFactory;

    @BeforeClass
    public static void beforeClass() throws Exception {
        ourCtx = new FhirContext(Patient.class);
    }

    @Before
    public void setUp() throws Exception {
        // DiagnosticReport
        //     Performer(practitioner)
        //     Subject(patient)
        //     Result(observation)
        //         Specimen(specimen1)
        //             Subject(patient)
        //             Collector(practitioner)
        //         Subject(patient)
        //         Performer(practitioner)
        //     Specimen(specimen2)

        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport.setId("DiagnosticReport/1");

        Observation observation = new Observation();
        observation.setId("Observation/1");

        Specimen specimen1 = new Specimen();
        specimen1.setId("Specimen/1");

        Specimen specimen2 = new Specimen();
        specimen2.setId("Specimen/2");

        Practitioner practitioner = new Practitioner();
        practitioner.setId("Practitioner/1");

        Patient patient = new Patient();
        patient.setId("Patient/1");

        diagnosticReport.setPerformer(new ResourceReferenceDt(practitioner));
        diagnosticReport.setSubject(new ResourceReferenceDt(patient));
        diagnosticReport.setResult(Arrays.asList(new ResourceReferenceDt[]{new ResourceReferenceDt(observation)}));
        diagnosticReport.setSpecimen(Arrays.asList(new ResourceReferenceDt(specimen2)));

        observation.setSpecimen(new ResourceReferenceDt(specimen1));
        observation.setSubject(new ResourceReferenceDt(patient));
        observation.setPerformer(Arrays.asList(new ResourceReferenceDt[]{new ResourceReferenceDt(practitioner)}));

        specimen1.setSubject(new ResourceReferenceDt(patient));
        specimen1.getCollection().setCollector(new ResourceReferenceDt(practitioner));

        myResourceList = Arrays.asList(new IResource[]{diagnosticReport});

        myBundleFactory = new Dstu2BundleFactory(ourCtx);
    }

    @Test
    public void whenIncludeIsAsterisk_bundle_shouldContainAllReferencedResources() throws Exception {
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_INCLUDES, includes(new String("*")));

        assertEquals(6, bundle.getEntry().size());
        assertEquals(2, numberOfEntriesOfType(bundle, Specimen.class));
    }

    @Test
    public void whenIncludeIsNull_bundle_shouldOnlyContainPrimaryResource() throws Exception {
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_INCLUDES, null);
        assertEquals(1, bundle.getEntry().size());
        assertEquals(1, numberOfEntriesOfType(bundle, DiagnosticReport.class));
    }

    @Test
    public void whenIncludeIsDiagnosticReportSubject_bundle_shouldIncludePatient() throws Exception {
        Set<Include> includes = includes(DiagnosticReport.INCLUDE_SUBJECT.getValue());
		Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_INCLUDES, includes);

        assertEquals(2, bundle.getEntry().size());
        assertEquals(1, numberOfEntriesOfType(bundle, DiagnosticReport.class));
        assertEquals(1, numberOfEntriesOfType(bundle, Patient.class));
    }

    @Test
    public void whenAChainedResourceIsIncludedAndItsParentIsAlsoIncluded_bundle_shouldContainTheChainedResource() throws Exception {
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_INCLUDES, includes(DiagnosticReport.INCLUDE_RESULT.getValue(), Observation.INCLUDE_SPECIMEN.getValue()));

        assertEquals(3, bundle.getEntry().size());
        assertEquals(1, numberOfEntriesOfType(bundle, DiagnosticReport.class));
        assertEquals(1, numberOfEntriesOfType(bundle, Observation.class));
        assertEquals(1, numberOfEntriesOfType(bundle, Specimen.class));
        List<Specimen> specimens = getResourcesOfType(bundle, Specimen.class);
        assertEquals(1, specimens.size());
        assertEquals("1", specimens.get(0).getId().getIdPart());
    }

    @Test
    public void whenAChainedResourceIsIncludedButItsParentIsNot_bundle_shouldNotContainTheChainedResource() throws Exception {
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_INCLUDES, includes(Observation.INCLUDE_SPECIMEN.getValue()));

        assertEquals(1, bundle.getEntry().size());
        assertEquals(1, numberOfEntriesOfType(bundle, DiagnosticReport.class));
    }

    @Test
    public void whenBundleInclusionRuleSetToResourcePresence_bundle_shouldContainAllResources() throws Exception {
        Bundle bundle = makeBundle(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE, null);

        assertEquals(6, bundle.getEntry().size());
        assertEquals(2, numberOfEntriesOfType(bundle, Specimen.class));
    }

    Bundle makeBundle(BundleInclusionRule theBundleInclusionRule, Set<Include> theIncludes) {
        myBundleFactory.addResourcesToBundle(myResourceList, null, "http://foo", theBundleInclusionRule, theIncludes);
        return (Bundle) myBundleFactory.getResourceBundle();
    }

    private Set<Include> includes(String... includes) {
        Set<Include> includeSet = new HashSet<Include>();
        for (String include : includes) {
            includeSet.add(new Include(include));
        }
        return includeSet;
    }

    private <T extends IResource> int numberOfEntriesOfType(Bundle theBundle, Class<T> theResourceClass) {
        int count = 0;
        for (Bundle.Entry entry : theBundle.getEntry()) {
            if (theResourceClass.isAssignableFrom(entry.getResource().getClass()))
                count++;
        }
        return count;
    }

    private <T extends IResource> List<T> getResourcesOfType(Bundle theBundle, Class<T> theResourceClass) {
        List<T> resources = new ArrayList<T>();
        for (Bundle.Entry entry : theBundle.getEntry()) {
            if (theResourceClass.isAssignableFrom(entry.getResource().getClass())) {
                resources.add((T) entry.getResource());
            }
        }
        return resources;
    }
}