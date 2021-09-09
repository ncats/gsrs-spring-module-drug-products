package gov.hhs.gsrs.products.productelist.models;

import gsrs.model.AbstractGsrsEntity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@IdClass(ProductActiveCompositePrimaryKeyId.class)
@Table(name="ELIST_PROD_ACTIVE_INGRED_MV")
public class ProductActiveElist extends AbstractGsrsEntity {

    @Id
    public String productId;

    @Id
    public String documentId;

    @Id
    public String name;

    @Id
    public String strengthNumber;

    @Id
    public String originalNumeratorNumber;

    @Id
    public String strengthDenominatorUnit;

    @Column(name="SUBSTANCEUNII")
    public String unii;

    @Column(name="ACTIVEMOIETY_1_NAME")
    public String activeMoietyName;

    @Column(name="STRENGTHNUMERATORUNIT")
    public String strengthNumeratorUnit;

    public ProductActiveElist () {}
}
