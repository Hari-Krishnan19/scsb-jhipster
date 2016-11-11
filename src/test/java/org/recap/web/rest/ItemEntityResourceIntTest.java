package org.recap.web.rest;

import org.recap.ScsbhipsterApp;

import org.recap.domain.ItemEntity;
import org.recap.repository.ItemEntityRepository;
import org.recap.service.ItemEntityService;
import org.recap.repository.search.ItemEntitySearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ItemEntityResource REST controller.
 *
 * @see ItemEntityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScsbhipsterApp.class)
public class ItemEntityResourceIntTest {

    private static final Integer DEFAULT_ITEM_ID = 1;
    private static final Integer UPDATED_ITEM_ID = 2;

    private static final String DEFAULT_BARCODE = "AAAAA";
    private static final String UPDATED_BARCODE = "BBBBB";

    private static final String DEFAULT_CUSTOMER_CODE = "AAAAA";
    private static final String UPDATED_CUSTOMER_CODE = "BBBBB";

    private static final String DEFAULT_CALL_NUMBER = "AAAAA";
    private static final String UPDATED_CALL_NUMBER = "BBBBB";

    private static final String DEFAULT_CALL_NUMBER_TYPE = "AAAAA";
    private static final String UPDATED_CALL_NUMBER_TYPE = "BBBBB";

    private static final Integer DEFAULT_ITEM_AVAILABILITY_STATUS_ID = 1;
    private static final Integer UPDATED_ITEM_AVAILABILITY_STATUS_ID = 2;

    private static final Integer DEFAULT_COPY_NUMBER = 1;
    private static final Integer UPDATED_COPY_NUMBER = 2;

    private static final Integer DEFAULT_OWNING_INSTITUTION_ID = 1;
    private static final Integer UPDATED_OWNING_INSTITUTION_ID = 2;

    private static final Integer DEFAULT_COLLECTION_GROUP_ID = 1;
    private static final Integer UPDATED_COLLECTION_GROUP_ID = 2;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";

    private static final LocalDate DEFAULT_LAST_UPDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LAST_UPDATED_BY = "AAAAA";
    private static final String UPDATED_LAST_UPDATED_BY = "BBBBB";

    private static final String DEFAULT_USE_RESTRICTIONS = "AAAAA";
    private static final String UPDATED_USE_RESTRICTIONS = "BBBBB";

    private static final String DEFAULT_VOLUME_PART_YEAR = "AAAAA";
    private static final String UPDATED_VOLUME_PART_YEAR = "BBBBB";

    private static final String DEFAULT_OWNING_INSTITUTION_ITEM_ID = "AAAAA";
    private static final String UPDATED_OWNING_INSTITUTION_ITEM_ID = "BBBBB";

    @Inject
    private ItemEntityRepository itemEntityRepository;

    @Inject
    private ItemEntityService itemEntityService;

    @Inject
    private ItemEntitySearchRepository itemEntitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restItemEntityMockMvc;

    private ItemEntity itemEntity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemEntityResource itemEntityResource = new ItemEntityResource();
        ReflectionTestUtils.setField(itemEntityResource, "itemEntityService", itemEntityService);
        this.restItemEntityMockMvc = MockMvcBuilders.standaloneSetup(itemEntityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemEntity createEntity(EntityManager em) {
        ItemEntity itemEntity = new ItemEntity()
                .itemId(DEFAULT_ITEM_ID)
                .barcode(DEFAULT_BARCODE)
                .customerCode(DEFAULT_CUSTOMER_CODE)
                .callNumber(DEFAULT_CALL_NUMBER)
                .callNumberType(DEFAULT_CALL_NUMBER_TYPE)
                .itemAvailabilityStatusId(DEFAULT_ITEM_AVAILABILITY_STATUS_ID)
                .copyNumber(DEFAULT_COPY_NUMBER)
                .owningInstitutionId(DEFAULT_OWNING_INSTITUTION_ID)
                .collectionGroupId(DEFAULT_COLLECTION_GROUP_ID)
                .createdDate(DEFAULT_CREATED_DATE)
                .createdBy(DEFAULT_CREATED_BY)
                .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
                .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
                .useRestrictions(DEFAULT_USE_RESTRICTIONS)
                .volumePartYear(DEFAULT_VOLUME_PART_YEAR)
                .owningInstitutionItemId(DEFAULT_OWNING_INSTITUTION_ITEM_ID);
        return itemEntity;
    }

    @Before
    public void initTest() {
        itemEntitySearchRepository.deleteAll();
        itemEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemEntity() throws Exception {
        int databaseSizeBeforeCreate = itemEntityRepository.findAll().size();

        // Create the ItemEntity

        restItemEntityMockMvc.perform(post("/api/item-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemEntity)))
                .andExpect(status().isCreated());

        // Validate the ItemEntity in the database
        List<ItemEntity> itemEntities = itemEntityRepository.findAll();
        assertThat(itemEntities).hasSize(databaseSizeBeforeCreate + 1);
        ItemEntity testItemEntity = itemEntities.get(itemEntities.size() - 1);
        assertThat(testItemEntity.getItemId()).isEqualTo(DEFAULT_ITEM_ID);
        assertThat(testItemEntity.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testItemEntity.getCustomerCode()).isEqualTo(DEFAULT_CUSTOMER_CODE);
        assertThat(testItemEntity.getCallNumber()).isEqualTo(DEFAULT_CALL_NUMBER);
        assertThat(testItemEntity.getCallNumberType()).isEqualTo(DEFAULT_CALL_NUMBER_TYPE);
        assertThat(testItemEntity.getItemAvailabilityStatusId()).isEqualTo(DEFAULT_ITEM_AVAILABILITY_STATUS_ID);
        assertThat(testItemEntity.getCopyNumber()).isEqualTo(DEFAULT_COPY_NUMBER);
        assertThat(testItemEntity.getOwningInstitutionId()).isEqualTo(DEFAULT_OWNING_INSTITUTION_ID);
        assertThat(testItemEntity.getCollectionGroupId()).isEqualTo(DEFAULT_COLLECTION_GROUP_ID);
        assertThat(testItemEntity.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemEntity.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testItemEntity.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testItemEntity.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testItemEntity.getUseRestrictions()).isEqualTo(DEFAULT_USE_RESTRICTIONS);
        assertThat(testItemEntity.getVolumePartYear()).isEqualTo(DEFAULT_VOLUME_PART_YEAR);
        assertThat(testItemEntity.getOwningInstitutionItemId()).isEqualTo(DEFAULT_OWNING_INSTITUTION_ITEM_ID);

        // Validate the ItemEntity in ElasticSearch
        ItemEntity itemEntityEs = itemEntitySearchRepository.findOne(testItemEntity.getId());
        assertThat(itemEntityEs).isEqualToComparingFieldByField(testItemEntity);
    }

    @Test
    @Transactional
    public void checkItemIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemEntityRepository.findAll().size();
        // set the field null
        itemEntity.setItemId(null);

        // Create the ItemEntity, which fails.

        restItemEntityMockMvc.perform(post("/api/item-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemEntity)))
                .andExpect(status().isBadRequest());

        List<ItemEntity> itemEntities = itemEntityRepository.findAll();
        assertThat(itemEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemAvailabilityStatusIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemEntityRepository.findAll().size();
        // set the field null
        itemEntity.setItemAvailabilityStatusId(null);

        // Create the ItemEntity, which fails.

        restItemEntityMockMvc.perform(post("/api/item-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemEntity)))
                .andExpect(status().isBadRequest());

        List<ItemEntity> itemEntities = itemEntityRepository.findAll();
        assertThat(itemEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOwningInstitutionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemEntityRepository.findAll().size();
        // set the field null
        itemEntity.setOwningInstitutionId(null);

        // Create the ItemEntity, which fails.

        restItemEntityMockMvc.perform(post("/api/item-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemEntity)))
                .andExpect(status().isBadRequest());

        List<ItemEntity> itemEntities = itemEntityRepository.findAll();
        assertThat(itemEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOwningInstitutionItemIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemEntityRepository.findAll().size();
        // set the field null
        itemEntity.setOwningInstitutionItemId(null);

        // Create the ItemEntity, which fails.

        restItemEntityMockMvc.perform(post("/api/item-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemEntity)))
                .andExpect(status().isBadRequest());

        List<ItemEntity> itemEntities = itemEntityRepository.findAll();
        assertThat(itemEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemEntities() throws Exception {
        // Initialize the database
        itemEntityRepository.saveAndFlush(itemEntity);

        // Get all the itemEntities
        restItemEntityMockMvc.perform(get("/api/item-entities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(itemEntity.getId().intValue())))
                .andExpect(jsonPath("$.[*].itemId").value(hasItem(DEFAULT_ITEM_ID)))
                .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
                .andExpect(jsonPath("$.[*].customerCode").value(hasItem(DEFAULT_CUSTOMER_CODE.toString())))
                .andExpect(jsonPath("$.[*].callNumber").value(hasItem(DEFAULT_CALL_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].callNumberType").value(hasItem(DEFAULT_CALL_NUMBER_TYPE.toString())))
                .andExpect(jsonPath("$.[*].itemAvailabilityStatusId").value(hasItem(DEFAULT_ITEM_AVAILABILITY_STATUS_ID)))
                .andExpect(jsonPath("$.[*].copyNumber").value(hasItem(DEFAULT_COPY_NUMBER)))
                .andExpect(jsonPath("$.[*].owningInstitutionId").value(hasItem(DEFAULT_OWNING_INSTITUTION_ID)))
                .andExpect(jsonPath("$.[*].collectionGroupId").value(hasItem(DEFAULT_COLLECTION_GROUP_ID)))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
                .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(DEFAULT_LAST_UPDATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.toString())))
                .andExpect(jsonPath("$.[*].useRestrictions").value(hasItem(DEFAULT_USE_RESTRICTIONS.toString())))
                .andExpect(jsonPath("$.[*].volumePartYear").value(hasItem(DEFAULT_VOLUME_PART_YEAR.toString())))
                .andExpect(jsonPath("$.[*].owningInstitutionItemId").value(hasItem(DEFAULT_OWNING_INSTITUTION_ITEM_ID.toString())));
    }

    @Test
    @Transactional
    public void getItemEntity() throws Exception {
        // Initialize the database
        itemEntityRepository.saveAndFlush(itemEntity);

        // Get the itemEntity
        restItemEntityMockMvc.perform(get("/api/item-entities/{id}", itemEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemEntity.getId().intValue()))
            .andExpect(jsonPath("$.itemId").value(DEFAULT_ITEM_ID))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.customerCode").value(DEFAULT_CUSTOMER_CODE.toString()))
            .andExpect(jsonPath("$.callNumber").value(DEFAULT_CALL_NUMBER.toString()))
            .andExpect(jsonPath("$.callNumberType").value(DEFAULT_CALL_NUMBER_TYPE.toString()))
            .andExpect(jsonPath("$.itemAvailabilityStatusId").value(DEFAULT_ITEM_AVAILABILITY_STATUS_ID))
            .andExpect(jsonPath("$.copyNumber").value(DEFAULT_COPY_NUMBER))
            .andExpect(jsonPath("$.owningInstitutionId").value(DEFAULT_OWNING_INSTITUTION_ID))
            .andExpect(jsonPath("$.collectionGroupId").value(DEFAULT_COLLECTION_GROUP_ID))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(DEFAULT_LAST_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.toString()))
            .andExpect(jsonPath("$.useRestrictions").value(DEFAULT_USE_RESTRICTIONS.toString()))
            .andExpect(jsonPath("$.volumePartYear").value(DEFAULT_VOLUME_PART_YEAR.toString()))
            .andExpect(jsonPath("$.owningInstitutionItemId").value(DEFAULT_OWNING_INSTITUTION_ITEM_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemEntity() throws Exception {
        // Get the itemEntity
        restItemEntityMockMvc.perform(get("/api/item-entities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemEntity() throws Exception {
        // Initialize the database
        itemEntityService.save(itemEntity);

        int databaseSizeBeforeUpdate = itemEntityRepository.findAll().size();

        // Update the itemEntity
        ItemEntity updatedItemEntity = itemEntityRepository.findOne(itemEntity.getId());
        updatedItemEntity
                .itemId(UPDATED_ITEM_ID)
                .barcode(UPDATED_BARCODE)
                .customerCode(UPDATED_CUSTOMER_CODE)
                .callNumber(UPDATED_CALL_NUMBER)
                .callNumberType(UPDATED_CALL_NUMBER_TYPE)
                .itemAvailabilityStatusId(UPDATED_ITEM_AVAILABILITY_STATUS_ID)
                .copyNumber(UPDATED_COPY_NUMBER)
                .owningInstitutionId(UPDATED_OWNING_INSTITUTION_ID)
                .collectionGroupId(UPDATED_COLLECTION_GROUP_ID)
                .createdDate(UPDATED_CREATED_DATE)
                .createdBy(UPDATED_CREATED_BY)
                .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
                .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
                .useRestrictions(UPDATED_USE_RESTRICTIONS)
                .volumePartYear(UPDATED_VOLUME_PART_YEAR)
                .owningInstitutionItemId(UPDATED_OWNING_INSTITUTION_ITEM_ID);

        restItemEntityMockMvc.perform(put("/api/item-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedItemEntity)))
                .andExpect(status().isOk());

        // Validate the ItemEntity in the database
        List<ItemEntity> itemEntities = itemEntityRepository.findAll();
        assertThat(itemEntities).hasSize(databaseSizeBeforeUpdate);
        ItemEntity testItemEntity = itemEntities.get(itemEntities.size() - 1);
        assertThat(testItemEntity.getItemId()).isEqualTo(UPDATED_ITEM_ID);
        assertThat(testItemEntity.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testItemEntity.getCustomerCode()).isEqualTo(UPDATED_CUSTOMER_CODE);
        assertThat(testItemEntity.getCallNumber()).isEqualTo(UPDATED_CALL_NUMBER);
        assertThat(testItemEntity.getCallNumberType()).isEqualTo(UPDATED_CALL_NUMBER_TYPE);
        assertThat(testItemEntity.getItemAvailabilityStatusId()).isEqualTo(UPDATED_ITEM_AVAILABILITY_STATUS_ID);
        assertThat(testItemEntity.getCopyNumber()).isEqualTo(UPDATED_COPY_NUMBER);
        assertThat(testItemEntity.getOwningInstitutionId()).isEqualTo(UPDATED_OWNING_INSTITUTION_ID);
        assertThat(testItemEntity.getCollectionGroupId()).isEqualTo(UPDATED_COLLECTION_GROUP_ID);
        assertThat(testItemEntity.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemEntity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testItemEntity.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testItemEntity.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testItemEntity.getUseRestrictions()).isEqualTo(UPDATED_USE_RESTRICTIONS);
        assertThat(testItemEntity.getVolumePartYear()).isEqualTo(UPDATED_VOLUME_PART_YEAR);
        assertThat(testItemEntity.getOwningInstitutionItemId()).isEqualTo(UPDATED_OWNING_INSTITUTION_ITEM_ID);

        // Validate the ItemEntity in ElasticSearch
        ItemEntity itemEntityEs = itemEntitySearchRepository.findOne(testItemEntity.getId());
        assertThat(itemEntityEs).isEqualToComparingFieldByField(testItemEntity);
    }

    @Test
    @Transactional
    public void deleteItemEntity() throws Exception {
        // Initialize the database
        itemEntityService.save(itemEntity);

        int databaseSizeBeforeDelete = itemEntityRepository.findAll().size();

        // Get the itemEntity
        restItemEntityMockMvc.perform(delete("/api/item-entities/{id}", itemEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean itemEntityExistsInEs = itemEntitySearchRepository.exists(itemEntity.getId());
        assertThat(itemEntityExistsInEs).isFalse();

        // Validate the database is empty
        List<ItemEntity> itemEntities = itemEntityRepository.findAll();
        assertThat(itemEntities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchItemEntity() throws Exception {
        // Initialize the database
        itemEntityService.save(itemEntity);

        // Search the itemEntity
        restItemEntityMockMvc.perform(get("/api/_search/item-entities?query=id:" + itemEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemId").value(hasItem(DEFAULT_ITEM_ID)))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
            .andExpect(jsonPath("$.[*].customerCode").value(hasItem(DEFAULT_CUSTOMER_CODE.toString())))
            .andExpect(jsonPath("$.[*].callNumber").value(hasItem(DEFAULT_CALL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].callNumberType").value(hasItem(DEFAULT_CALL_NUMBER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].itemAvailabilityStatusId").value(hasItem(DEFAULT_ITEM_AVAILABILITY_STATUS_ID)))
            .andExpect(jsonPath("$.[*].copyNumber").value(hasItem(DEFAULT_COPY_NUMBER)))
            .andExpect(jsonPath("$.[*].owningInstitutionId").value(hasItem(DEFAULT_OWNING_INSTITUTION_ID)))
            .andExpect(jsonPath("$.[*].collectionGroupId").value(hasItem(DEFAULT_COLLECTION_GROUP_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(DEFAULT_LAST_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.toString())))
            .andExpect(jsonPath("$.[*].useRestrictions").value(hasItem(DEFAULT_USE_RESTRICTIONS.toString())))
            .andExpect(jsonPath("$.[*].volumePartYear").value(hasItem(DEFAULT_VOLUME_PART_YEAR.toString())))
            .andExpect(jsonPath("$.[*].owningInstitutionItemId").value(hasItem(DEFAULT_OWNING_INSTITUTION_ITEM_ID.toString())));
    }
}
