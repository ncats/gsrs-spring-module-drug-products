package gov.hhs.gsrs.products.productelist.models;

import javax.persistence.Column;
import java.io.Serializable;

public class ProductRouteCompositePrimaryKeyId implements Serializable {

    @Column(name="PRODUCTID")
    private String productId;

    @Column(name="ROUTE_CODE")
    public String routeCode;

    public ProductRouteCompositePrimaryKeyId() {};

    // default constructor
    public ProductRouteCompositePrimaryKeyId(String productId, String routeCode) {
        this.productId = productId;
        this.routeCode = routeCode;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductRouteCompositePrimaryKeyId that = (ProductRouteCompositePrimaryKeyId) o;

        if (!productId.equals(that.productId)) return false;
        return (routeCode.equals(that.routeCode));
    }

    @Override
    public int hashCode() {
        int result =  productId.hashCode();
        result = 31 * result + (routeCode != null ? routeCode.hashCode() : 0);
        return (int) result;
    }

}