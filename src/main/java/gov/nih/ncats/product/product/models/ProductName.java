package gov.nih.ncats.product.product.models;

import lombok.Data;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name="SRSCID_PRODUCT_NAME", schema = "srscid")
public class ProductName extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodNameSeq", sequenceName="SRSCID.SRSCID_SQ_PRODUCT_NAME_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodNameSeq")
    @Column(name="PRODUCT_NAME_ID")
    public Long id;

    @Column(name="PRODUCT_NAME")
    public String productName;

    @Column(name="PRODUCT_NAME_TYPE")
    public String productNameType;

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

    @JoinColumn(name = "PRODUCT_NAME_ID", referencedColumnName = "PRODUCT_NAME_ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductTermAndPart> productTermAndTermPartList = new ArrayList<ProductTermAndPart>();

}
