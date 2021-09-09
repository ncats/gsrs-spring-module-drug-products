package gov.hhs.gsrs.products.product.models;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name="SRSCID_PRODUCT_LOT")
public class ProductLot extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodLotSeq", sequenceName="SRSCID_SQ_PRODUCT_LOT_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodLotSeq")
    @Column(name="PRODUCT_LOT_ID")
    public Long id;

    @Column(name="LOT_NO")
    public String lotNo;

    @Column(name="LOT_SIZE")
    public String lotSize;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="EXPIRY_DATE")
    public Date expiryDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="MANUFACTURE_DATE")
    public Date manufactureDate;

    /*
    @Version
    public Long internalVersion;

    @Column(name = "CREATED_BY")
    public String createdBy;

    @Column(name = "MODIFIED_BY")
    public String modifiedBy;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @CreatedDate
    @Indexable( name = "Create Date", sortable=true)
    @Column(name = "CREATE_DATE")
    private Date creationDate;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @LastModifiedDate
    @Indexable( name = "Last Modified Date", sortable=true)
    @Column(name = "MODIFY_DATE")
    private Date lastModifiedDate;
    */

    @JoinColumn(name = "PRODUCT_LOT_ID", referencedColumnName = "PRODUCT_LOT_ID")
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    public List<ProductIngredient> productIngredientList = new ArrayList<ProductIngredient>();
}
