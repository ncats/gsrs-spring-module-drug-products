package gov.nih.ncats.product.productelist.models;

import javax.persistence.Column;
import java.io.Serializable;

public class ProductInactiveCompositePrimaryKeyId implements Serializable {

    @Column(name="PRODUCTID")
    private String productId;

    @Column(name="DOCUMENTID")
    private String documentId;

    @Column(name="SUBSTANCENAME")
    private String name;

   // @Column(name="STRENGTHNUMBER")
   // private String strengthNumber;

    //@Column(name="ORIGINALNUMERATORNUMBER")
    //private String originalNumeratorNumber;

    public ProductInactiveCompositePrimaryKeyId() {};

    // default constructor
    public ProductInactiveCompositePrimaryKeyId(String productId, String documentId, String name) {
        this.productId = productId;
        this.documentId = documentId;
        this.name = name;
     //   this.strengthNumber = strengthNumber;
      //  this.originalNumeratorNumber = originalNumeratorNumber;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductInactiveCompositePrimaryKeyId that = (ProductInactiveCompositePrimaryKeyId) o;

        if (!productId.equals(that.productId)) return false;
        if (!documentId.equals(that.documentId)) return false;
    //    if (!name.equals(that.name)) return false;

       // if (!strengthNumber.equals(that.strengthNumber)) return false;
        return (name.equals(that.name));
    }

    @Override
    public int hashCode() {
        int result =  productId.hashCode();
        result = 31 * result + (documentId != null ? documentId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
      //  result = 31 * result + (strengthNumber != null ? strengthNumber.hashCode() : 0);
      //  result = 31 * result + (originalNumeratorNumber != null ? originalNumeratorNumber.hashCode() : 0);
        return (int) result;
    }

}