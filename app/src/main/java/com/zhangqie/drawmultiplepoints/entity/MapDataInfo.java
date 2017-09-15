package com.zhangqie.drawmultiplepoints.entity;

import java.util.List;

/**
 * Created by zhangqie on 2017/8/15.
 */

public class MapDataInfo {

    private List<Data> data;

    public void setData(List<Data> data){
        this.data = data;
    }
    public List<Data> getData(){
        return this.data;
    }
    public class Data
    {
        private String lift_id;

        private String lift_code;

        private String next_check_date;

        private String icon;

        private String use_company_name;

        private List<String> position;

        private String use_state;

        public void setLift_id(String lift_id){
            this.lift_id = lift_id;
        }
        public String getLift_id(){
            return this.lift_id;
        }
        public void setLift_code(String lift_code){
            this.lift_code = lift_code;
        }
        public String getLift_code(){
            return this.lift_code;
        }
        public void setNext_check_date(String next_check_date){
            this.next_check_date = next_check_date;
        }
        public String getNext_check_date(){
            return this.next_check_date;
        }
        public void setIcon(String icon){
            this.icon = icon;
        }
        public String getIcon(){
            return this.icon;
        }
        public void setUse_company_name(String use_company_name){
            this.use_company_name = use_company_name;
        }
        public String getUse_company_name(){
            return this.use_company_name;
        }
        public void setPosition(List<String> position){
            this.position = position;
        }
        public List<String> getPosition(){
            return this.position;
        }
        public void setUse_state(String use_state){
            this.use_state = use_state;
        }
        public String getUse_state(){
            return this.use_state;
        }
    }

}
