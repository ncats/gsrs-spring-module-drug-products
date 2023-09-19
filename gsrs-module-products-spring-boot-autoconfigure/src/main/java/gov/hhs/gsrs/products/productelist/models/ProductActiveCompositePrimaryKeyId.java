package gov.hhs.gsrs.products.productelist.models;

import javax.persistence.Column;
import java.io.Serializable;

public class ProductActiveCompositePrimaryKeyId implements Serializable {

    @Column(name="PRODUCTID", length = 100)
    private String productId;

    @Column(name="DOCUMENTID", length = 100)
    private String documentId;

    @Column(name="SUBSTANCENAME", length=400)
    private String name;

    @Column(name="STRENGTHNUMBER", length = 50)
    private String strengthNumber;

    @Column(name="ORIGINALNUMERATORNUMBER", length = 50)
    private String originalNumeratorNumber;

    @Column(name="STRENGTHDENOMINATORUNIT", length = 50)
    private String strengthDenominatorUnit;

    public ProductActiveCompositePrimaryKeyId() {};

    // default constructor
    public ProductActiveCompositePrimaryKeyId(String productId, String documentId, String name,
                                              String strengthNumber, String originalNumeratorNumber, String strengthDenominatorUnit) {
        this.productId = productId;
        this.documentId = documentId;
        this.name = name;
        this.strengthNumber = strengthNumber;
        this.originalNumeratorNumber = originalNumeratorNumber;
        this.strengthDenominatorUnit = strengthDenominatorUnit;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductActiveCompositePrimaryKeyId that = (ProductActiveCompositePrimaryKeyId) o;

        if (!productId.equals(that.productId)) return false;
        if (!documentId.equals(that.documentId)) return false;
        if (!name.equals(that.name)) return false;

        if (!strengthNumber.equals(that.strengthNumber)) return false;
        if (!originalNumeratorNumber.equals(that.originalNumeratorNumber)) return false;
        return (strengthDenominatorUnit.equals(that.strengthDenominatorUnit));
    }

    @Override
    public int hashCode() {
        int result =  productId.hashCode();
        result = 31 * result + (documentId != null ? documentId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (strengthNumber != null ? strengthNumber.hashCode() : 0);
        result = 31 * result + (originalNumeratorNumber != null ? originalNumeratorNumber.hashCode() : 0);
        result = 31 * result + (strengthDenominatorUnit != null ? strengthDenominatorUnit.hashCode() : 0);
        return (int) result;
    }

}