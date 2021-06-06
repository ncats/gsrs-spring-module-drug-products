package gov.nih.ncats.product.productall.models;

import gsrs.GsrsEntityProcessorListener;
import gsrs.model.AbstractGsrsEntity;
import gsrs.model.AbstractGsrsManualDirtyEntity;
import ix.core.models.Indexable;
import ix.core.models.IxModel;
import ix.core.search.text.TextIndexerEntityListener;
import ix.ginas.models.serialization.GsrsDateDeserializer;
import ix.ginas.models.serialization.GsrsDateSerializer;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name="SRSCID_PRODUCT_ING_ALL_TWO_MV", schema = "srscid")
public class ProductIngredientAll extends AbstractGsrsEntity {

    @Id
    @Column(name="ID")
    public String id;

    @Column(name="PRODUCTID")
    public String productId;

    /*
    @Column(name="BDNUM")
    public String bdnum;

    @Column(name="BASISOFSTRENGTH")
    public String basisOfStrengthBdnum;
    */

    @Indexable(facet = true, name = "Substance Key")
    @Column(name="SUBSTANCE_KEY")
    public String substanceKey;

    @Indexable
    @Column(name="BOS_SUBSTANCE_KEY")
    public String basisOfStrengthSubstanceKey ;

    @Indexable(facet=true, name= "Ingredient Name", sortable = true)
    @Column(name="SUBSTANCENAME")
    public String substanceName;

    @Indexable(facet=true, name= "Ingredient Approval ID")
    @Column(name="SUBSTANCE_APPROVAL_ID")
    public String ingredientApprovalId;

    @Indexable
    @Column(name="SUBSTANCE_UUID")
    public String substanceUuid;

    @Indexable(facet=true, name= "Ingredient Type")
    @Column(name="INGREDIENTTYPE")
    public String ingredientType;

    @Indexable(facet=true, name= "Active Moiety")
    @Column(name="ACTIVEMOIETY_1_NAME")
    public String activeMoietyName;

    @Column(name="ACTIVEMOIETY_1_UNII")
    public String activeMoietyUnii;

    @Column(name="STRENGTHNUMBER")
    public String strengthNumber;

    @Column(name="STRENGTHNUMERATORUNIT")
    public String strengthNumeratorUnit;

    @Indexable(facet=true, name= "Dosage Form Name", sortable = true)
    @Column(name="DOSAGEFORM")
    public String dosageFormName;

    @Column(name="FROMTABLE")
    public String fromTable;

    public ProductIngredientAll () {}
}
