package gov.hhs.gsrs.products.product.models;

import ix.core.models.*;
import ix.core.models.Principal;
import ix.core.models.Indexable;
import ix.core.models.IxModel;

import gsrs.GsrsEntityProcessorListener;
import gsrs.security.GsrsSecurityUtils;
import gsrs.model.AbstractGsrsEntity;
import gsrs.model.AbstractGsrsManualDirtyEntity;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@MappedSuperclass
public class ProductCommonData extends AbstractGsrsEntity {

  //  @Id
  //  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appSeq")
  //  public Long id;

   // @CreatedBy
    @Column(name = "CREATED_BY")
    public String createdBy;

    //@LastModifiedBy
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

    @Version
    @Column(name = "INTERNAL_VERSION")
    public Long internalVersion = 0L; // or 1L if you want to start at 1

    public ProductCommonData() {
    }

    @PrePersist
    public void prePersist() {
      //  Date currentDate = TimeUtil.getCurrentDate();
     /*
        Principal p1=UserFetcher.getActingUser();
        if (p1 != null) {
            this.createdBy = p1.username;
        }

        this.createDate = currentDate;
      */
       // this.createdBy = "ADMIN";
      //  if (createdBy == null) {
            // TO DO: Connect to Pricipal Object to get username
            /*
            Principal p1 = UserFetcher.getActingUser();
            if (p1 != null) {
                this.createdBy = p1.username;
            }
             */
      //     UserProfile userProfile = GsrsSecurityUtils.getCurrentUser().toString();

      //  this.modifiedBy = GsrsSecurityUtils.getCurrentUser().toString();
         // String username = GsrsSecurityUtils.getCurrentUser();

            this.createdBy = "ADMIN";
            this.modifiedBy = "ADMIN";
    }

    @PreUpdate
    public void preUpdate() {
        /*
        Date currentDate = TimeUtil.getCurrentDate();
        Principal p1=UserFetcher.getActingUser();
        if (p1 != null) {
            this.modifiedBy = p1.username;
        }

        this.modifyDate = currentDate;
         */
       // this.modifiedBy = "ADMIN";

       // if (modifiedBy == null) {
            // TO DO: Connect to Pricipal Object to get username
            /*
            Principal p1 = UserFetcher.getActingUser();
            if (p1 != null) {
                this.createdBy = p1.username;
            }
             */
            this.modifiedBy = "ADMIN";
     //   }
     //   else {
      //      this.modifiedBy = modifiedBy;
     //   }
    }

    /*
    public String getCreatedBy () {
        //Get from Database
        return this.createdBy;
    }
    */
    public void setCreatedBy (String createdBy) {
        //Store to Database
    //    if (createdBy == null) {
            // TO DO: Connect to Pricipal Object to get username
            /*
            Principal p1 = UserFetcher.getActingUser();
            if (p1 != null) {
                this.createdBy = p1.username;
            }
      //       */
      //      this.createdBy = "ADMIN";
     //   }
     //   else {
     //       this.createdBy = createdBy;
     //   }
    }

    /*
    public Date getCreationDate() {
        //Get from Database
        return this.creationDate;
    }
    */

    /*
    public void setCreateDate(Date createDate) {

        if (createDate == null) {
            this.createDate = TimeUtil.getCurrentDate();
        }
        else {
            this.createDate = createDate;
        }
    }
    */

   // public String getModifiedBy () {
    //    return this.modifiedBy;
  //  }

    public void setModifiedBy (String modifiedBy) {
    //    System.out.println("******************** INSIDE Modify By " + modifiedBy);
        //Store to Database
    //    if (modifiedBy == null) {
            // TO DO: Connect to Pricipal Object to get username
            /*
            Principal p1 = UserFetcher.getActingUser();
            if (p1 != null) {
                this.createdBy = p1.username;
            }
             */
      //      this.modifiedBy = "ADMIN";
      //  }
     //   else {
     //       this.modifiedBy = modifiedBy;
    //    }
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public Date getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    /*
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = TimeUtil.getCurrentDate();
    }
    */


    /*
    public Date getCreated() {
        return createDate;
    }

    public void setCreated(Date created) {
        this.createDate = created;
    }
    */

    public String convertDateToString(Date dtDate) {

        String strDate = null;
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            if (dtDate != null) {
                strDate = df.format(dtDate);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return strDate;
    }

    public Date convertStringToDate(String strDate) {

        Date dtDate = null;
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            if ((strDate != null) && (strDate.length() > 0)) {
                dtDate = df.parse(strDate);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return dtDate;
    }

}
