package com.apu.olga.clientapplication.firebase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

 @JsonIgnoreProperties({"mId"})
 class Tenders {

     private String mId;
     private String mTitle;

     public Tenders() {
     }

     public Tenders(String title) {
         mId = "some_id";
         this.mTitle = title;
     }

     public String getId() {
         return mId;
     }

     public void setId(String mId) {
         this.mId = mId;
     }

     public String getTitle() {
         return mTitle;
     }

     public void setTitle(String mTitle) {
         this.mTitle = mTitle;
     }
 }
