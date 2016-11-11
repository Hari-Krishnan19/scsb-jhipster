package org.recap.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.recap.ScsbhipsterApp;
import org.recap.domain.BibliographicEntity;
import org.recap.domain.InstitutionEntity;
import org.recap.repository.BibliographicEntityRepository;
import org.recap.repository.HoldingsEntityRepository;
import org.recap.repository.InstitutionEntityRepository;
import org.recap.repository.ItemEntityRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by harikrishnanv on 11/11/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScsbhipsterApp.class)
public class PersistingFromXMLToDBTest {

    //Repositories

    @Inject
    BibliographicEntityRepository bibliographicEntityRepository;

    @Inject
    HoldingsEntityRepository holdingsEntityRepository;

    @Inject
    ItemEntityRepository itemEntityRepository;

    @Inject
    InstitutionEntityRepository institutionEntityRepository;


    @Test
    public void checkSaveFromXMLToDb() throws Exception{

        //Perform Unmarshalling
        URL resource = getClass().getResource("/recap_records_1.xml");
        assertNotNull(resource);
        File file = new File(resource.toURI());
        JAXBContext jaxbContext = JAXBContext.newInstance(BibRecords.class);
        assertNotNull(jaxbContext);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        assertNotNull(jaxbContext);
        BibRecords bibRecords = (BibRecords) unmarshaller.unmarshal(file);
        List<BibRecord> bibRecordList = bibRecords.getBibRecords();

        //Populate
        List<BibliographicEntity> bibliographicEntities = new ArrayList<>();
        for (BibRecord bibRecord : bibRecordList) {
            BibliographicEntity bibliographicEntity = new BibliographicEntity();
//          InstitutionEntity institutionEntity = institutionEntityRepository.findByInstitutionCode(bibRecord.getBib().getOwningInstitutionId().getDescription());
            bibliographicEntity.setOwningInstitutionId(01);
            Marshaller marshaller = jaxbContext.createMarshaller();
            OutputStream os = new ByteArrayOutputStream();
            marshaller.marshal(bibRecord.getBib().getContent().getCollection(), os);
            bibliographicEntity.setContent(os.toString());
            bibliographicEntities.add(bibliographicEntity);
        }

    }

    @Test
    public void checkSaveForInstitution() throws Exception{
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setInstitutionId(2);
        institutionEntity.setInstitutionCode("CUL");
        institutionEntity.setInstitutionName("ColumbiaUniversityLibrary");
        institutionEntityRepository.save(institutionEntity);
    }
}
