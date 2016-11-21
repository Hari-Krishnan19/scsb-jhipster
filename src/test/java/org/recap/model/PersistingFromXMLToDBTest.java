package org.recap.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.recap.ScsbhipsterApp;
import org.recap.domain.BibliographicEntity;
import org.recap.domain.HoldingsEntity;
import org.recap.domain.InstitutionEntity;
import org.recap.domain.ItemEntity;
import org.recap.repository.BibliographicEntityRepository;
import org.recap.repository.HoldingsEntityRepository;
import org.recap.repository.InstitutionEntityRepository;
import org.recap.repository.ItemEntityRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

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
    public void checkDelete() throws Exception {
        bibliographicEntityRepository.deleteAll();
        holdingsEntityRepository.deleteAll();
        itemEntityRepository.deleteAll();
    }

    @Test
    public void checkSaveForInstitution() throws Exception{
        institutionEntityRepository.deleteAll();
        InstitutionEntity institutionEntity1 = new InstitutionEntity();
        institutionEntity1.setInstitutionId(1);
        institutionEntity1.setInstitutionCode("PUL");
        institutionEntity1.setInstitutionName("PrincetonUniversityLibrary");
        institutionEntityRepository.save(institutionEntity1);
        InstitutionEntity institutionEntity2 = new InstitutionEntity();
        institutionEntity2.setInstitutionId(2);
        institutionEntity2.setInstitutionCode("CUL");
        institutionEntity2.setInstitutionName("ColumbiaUniversityLibrary");
        InstitutionEntity institutionEntity3 = new InstitutionEntity();
        institutionEntityRepository.save(institutionEntity2);
        institutionEntity3.setInstitutionId(3);
        institutionEntity3.setInstitutionCode("NYPL");
        institutionEntity3.setInstitutionName("NewYorkPublicLibrary");
        institutionEntityRepository.save(institutionEntity3);
    }

   


    @Test
    public void checkSaveFromXMLToDbUsingJAXB() throws Exception {

        //Perform Unmarshalling
       /* URL resource = getClass().getResource("/error.xml");
        System.out.println();
        assertNotNull(resource);*/
        File file = new File("/home/likewise-open/HTCINDIA/harikrishnanv/Documents/testxml");
        for (File files : file.listFiles()) {
            System.out.println(files.getAbsolutePath());

//        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
//        File file = new File(resource.toURI());
            JAXBContext jaxbContext = JAXBContext.newInstance(BibRecords.class);
            assertNotNull(jaxbContext);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            assertNotNull(jaxbContext);
            BibRecords bibRecords = (BibRecords) unmarshaller.unmarshal(files);
            List<BibRecord> bibRecordList = bibRecords.getBibRecords();

            //Additional Util Classes
            Random random = new Random();


            List<BibliographicEntity> bibliographicEntities = new ArrayList<>();
            for (BibRecord bibRecord : bibRecordList) {

                //For bibliographic_t

                LocalDate localDateTime = LocalDate.now();
                BibliographicEntity bibliographicEntity = new BibliographicEntity();
                InstitutionEntity institutionEntity;
                institutionEntity = institutionEntityRepository.findByInstitutionCode(bibRecord.getBib().getOwningInstitutionId().getDescription());
                String owningInstitutionBibId = String.valueOf(random.nextInt());
                bibliographicEntity.setOwningInstitutionBibId(owningInstitutionBibId);
                bibliographicEntity.setBibliographicId(random.nextInt());
                bibliographicEntity.setOwningInstitutionId(institutionEntity.getInstitutionId());
                Marshaller marshaller = jaxbContext.createMarshaller();
                OutputStream os = new ByteArrayOutputStream();
                marshaller.marshal(bibRecord.getBib().getContent().getCollection(), os);
                bibliographicEntity.setContent(os.toString());
                bibliographicEntity.setLastUpdatedDate(localDateTime);
                bibliographicEntity.setLastUpdatedBy("Admin");
                bibliographicEntity.setCreatedDate(localDateTime);
                bibliographicEntity.setCreatedBy("Admin");


                //for holdings
                List<Holdings> holdingsList = bibRecord.getHoldings();
                Set<HoldingsEntity> holdingsEntities = new HashSet<>();
                for (Holdings holdings : holdingsList) {
                    List<Holding> holdingList = holdings.getHoldingList();
                    for (Holding holding : holdingList) {
                        HoldingsEntity holdingsEntity = new HoldingsEntity();
                        holdingsEntity.setOwningInstitutionHoldingsId(holding.getOwningInstitutionHoldingsId());
                        OutputStream osForHoldingsContent = new ByteArrayOutputStream();
                        marshaller.marshal(holding.getHoldingContent().getCollection(), osForHoldingsContent);
                        holdingsEntity.setContent(osForHoldingsContent.toString());
                        holdingsEntity.setHoldingsId(random.nextInt());
                        holdingsEntity.setOwningInstitutionId(institutionEntity.getInstitutionId());
                        holdingsEntity.setCreatedDate(localDateTime);
                        holdingsEntity.setCreatedBy("admin");
                        holdingsEntity.setLastUpdatedDate(localDateTime);
                        holdingsEntity.setLastUpdatedBy("admin");
                        holdingsEntities.add(holdingsEntity);

                        //for items
                        List<Items> itemsList = holding.getItemsList();
                        Set<ItemEntity> itemEntities = new HashSet<>();
                        for (Items items : itemsList) {
                            List<Record> recordList = items.getContent().getCollection().getRecordList();
                            for (Record record : recordList) {
                                ItemEntity itemEntity = new ItemEntity();
                                itemEntity.setOwningInstitutionId(institutionEntity.getInstitutionId());
                                itemEntity.setOwningInstitutionItemId(String.valueOf(random.nextInt()));
                                itemEntity.setCreatedDate(localDateTime);
                                itemEntity.setCreatedBy("admin");
                                itemEntity.setLastUpdatedDate(localDateTime);
                                itemEntity.setLastUpdatedBy("admin");
                                for (DataField dataField : record.getDatafield()) {
                                    Integer tag = dataField.getTag();
                                    for (SubField subField : dataField.getSubFieldList()) {
                                        String code = subField.getCode();
                                        if ((tag == 876) && (code.equalsIgnoreCase("a"))) {
                                            itemEntity.setItemId(Integer.valueOf(subField.getDescription()));
                                        }
                                        if ((tag == 876) && (code.equalsIgnoreCase("h"))) {
                                            itemEntity.setUseRestrictions(subField.getDescription());
                                        }
                                        if ((tag == 876) && (code.equalsIgnoreCase("j"))) {
                                            if ((subField.getDescription().equalsIgnoreCase("Available"))) {
                                                itemEntity.setItemAvailabilityStatusId(01);
                                            } else {
                                                itemEntity.setItemAvailabilityStatusId(02);
                                            }
                                        }
                                        if ((tag == 876) && (code.equalsIgnoreCase("p"))) {
                                            itemEntity.setBarcode(subField.getDescription());
                                        }
                                        if ((tag == 876) && (code.equalsIgnoreCase("t"))) {
                                            itemEntity.setCopyNumber(Integer.valueOf(subField.getDescription()));
                                        }
                                        if ((tag == 876) && (code.equalsIgnoreCase("3"))) {
                                            itemEntity.setVolumePartYear(subField.getDescription());
                                        }
                                        if ((tag == 900) && (code.equalsIgnoreCase("a"))) {
                                            if (subField.getDescription().equalsIgnoreCase("open")) {
                                                itemEntity.setCollectionGroupId(01);
                                            } else if (subField.getDescription().equalsIgnoreCase("shared")) {
                                                itemEntity.setCollectionGroupId(02);
                                            } else if (subField.getDescription().equalsIgnoreCase("private")) {
                                                itemEntity.setCollectionGroupId(03);
                                            }
                                        }
                                        if ((tag == 900) && (code.equalsIgnoreCase("b"))) {
                                            itemEntity.setCustomerCode(subField.getDescription());
                                        }
                                    }
                                }
                                itemEntities.add(itemEntity);
                            }

                        }
                        holdingsEntity.setItemEntities(itemEntities);
                    }
                }
                bibliographicEntity.setHoldingsEntities(holdingsEntities);
                bibliographicEntities.add(bibliographicEntity);
            }

            bibliographicEntityRepository.save(bibliographicEntities);

            System.out.println("Saved Records");
        }
    }

}
