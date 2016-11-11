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
import org.recap.web.rest.UserResourceIntTest;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by harikrishnanv on 11/11/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScsbhipsterApp.class)
public class UnMarshallerTest {

    @Test
    public void checkUnmarshalling() throws Exception {

        URL resource = getClass().getResource("/recap_records_1.xml");
        assertNotNull(resource);
        File file = new File(resource.toURI());
        JAXBContext jaxbContext = JAXBContext.newInstance(BibRecords.class);
        assertNotNull(jaxbContext);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        assertNotNull(jaxbContext);
        BibRecords bibRecords = (BibRecords) unmarshaller.unmarshal(file);
        List<BibRecord> bibRecordList = bibRecords.getBibRecords();

        for (BibRecord bibRecord : bibRecordList) {
            System.out.println(bibRecord.getBib().getOwningInstitutionId().getDescription());
            List<Record> recordList = bibRecord.getBib().getContent().getCollection().getRecordList();
            for (Record record : recordList) {
                System.out.println("Leader :" + record.getLeader().getDescription());
                List<ControlField> controlFieldList = record.getControlfield();
                for (ControlField controlField : controlFieldList) {
                    System.out.println("controlField :" + controlField.getDescription());
                    System.out.print("Tag :" + controlField.getTag());
                }
                List<DataField> dataFieldList = record.getDatafield();
                for (DataField dataField : dataFieldList) {
                    System.out.println("Ind1 :" + dataField.getInd1());
                    System.out.print("Ind2 :" + dataField.getInd2());
                    System.out.print("Tag :" + dataField.getTag());
                }
            }
        }
    }
}


