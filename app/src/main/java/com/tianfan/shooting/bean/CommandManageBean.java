package com.tianfan.shooting.bean;

import java.util.List;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/26 23:59
 * 修改人：Chen
 * 修改时间：2020/4/26 23:59
 */
public class CommandManageBean {

    private List<CommandManageItem> datas;
    private boolean selectPostion;

    public boolean isSelectPostion() {
        return selectPostion;
    }

    public void setSelectPostion(boolean selectPostion) {
        this.selectPostion = selectPostion;
    }
    public List<CommandManageItem> getDatas() {
        return datas;
    }

    public void setDatas(List<CommandManageItem> datas) {
        this.datas = datas;
    }


    public static class CommandManageItem{

        /**
         * task_id : task_20200316221424504475
         * person_id : task_person_20200318235017285379
         * person_idno : 450100199901010002
         * person_name : 姓名2
         * person_orga : 工作单位2
         * person_role : 角色2
         * person_head :
         * person_row : 1
         * person_col : 2
         * person_status : 1
         * person_score : [{"task_id":"task_20200316221424504475","person_id":"task_person_20200318235017285379","rounds":1,"inuser":"user_0001","intime":1584942479000,"score_0":1,"score_5":1,"score_6":1,"score_7":1,"score_8":1,"score_9":1,"score_10":1,"all_count":7,"hit_count":6,"hit_score":45},{"task_id":"task_20200316221424504475","person_id":"task_person_20200318235017285379","rounds":2,"inuser":"user_0001","intime":1584942484000,"score_0":1,"score_5":1,"score_6":1,"score_7":1,"score_8":1,"score_9":1,"score_10":1,"all_count":7,"hit_count":6,"hit_score":45}]
         */

        private String task_id;
        private String task_rounds;
        private String person_id;
        private String person_idno;
        private String person_name;
        private String person_orga;
        private String person_role;
        private String person_head;
        private int person_row;
        private int person_col;
        private String person_status;
        private List<PersonScoreBean> person_score;


        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getTask_rounds() {
            return task_rounds;
        }

        public void setTask_rounds(String task_rounds) {
            this.task_rounds = task_rounds;
        }

        public String getPerson_id() {
            return person_id;
        }

        public void setPerson_id(String person_id) {
            this.person_id = person_id;
        }

        public String getPerson_idno() {
            return person_idno;
        }

        public void setPerson_idno(String person_idno) {
            this.person_idno = person_idno;
        }

        public String getPerson_name() {
            return person_name;
        }

        public void setPerson_name(String person_name) {
            this.person_name = person_name;
        }

        public String getPerson_orga() {
            return person_orga;
        }

        public void setPerson_orga(String person_orga) {
            this.person_orga = person_orga;
        }

        public String getPerson_role() {
            return person_role;
        }

        public void setPerson_role(String person_role) {
            this.person_role = person_role;
        }

        public String getPerson_head() {
            return person_head;
        }

        public void setPerson_head(String person_head) {
            this.person_head = person_head;
        }

        public int getPerson_row() {
            return person_row;
        }

        public void setPerson_row(int person_row) {
            this.person_row = person_row;
        }

        public int getPerson_col() {
            return person_col;
        }

        public void setPerson_col(int person_col) {
            this.person_col = person_col;
        }

        public String getPerson_status() {
            return person_status;
        }

        public void setPerson_status(String person_status) {
            this.person_status = person_status;
        }

        public List<PersonScoreBean> getPerson_score() {
            return person_score;
        }

        public void setPerson_score(List<PersonScoreBean> person_score) {
            this.person_score = person_score;
        }

        public static class PersonScoreBean {
            /**
             * task_id : task_20200316221424504475
             * person_id : task_person_20200318235017285379
             * rounds : 1
             * inuser : user_0001
             * intime : 1584942479000
             * score_0 : 1
             * score_5 : 1
             * score_6 : 1
             * score_7 : 1
             * score_8 : 1
             * score_9 : 1
             * score_10 : 1
             * all_count : 7
             * hit_count : 6
             * hit_score : 45
             */

            private String task_id;
            private String person_id;
            private int rounds;
            private String inuser;
            private long intime;
            private int score_0;
            private int score_5;
            private int score_6;
            private int score_7;
            private int score_8;
            private int score_9;
            private int score_10;
            private int all_count;
            private int hit_count;
            private int hit_score;

            public String getTask_id() {
                return task_id;
            }

            public void setTask_id(String task_id) {
                this.task_id = task_id;
            }

            public String getPerson_id() {
                return person_id;
            }

            public void setPerson_id(String person_id) {
                this.person_id = person_id;
            }

            public int getRounds() {
                return rounds;
            }

            public void setRounds(int rounds) {
                this.rounds = rounds;
            }

            public String getInuser() {
                return inuser;
            }

            public void setInuser(String inuser) {
                this.inuser = inuser;
            }

            public long getIntime() {
                return intime;
            }

            public void setIntime(long intime) {
                this.intime = intime;
            }

            public int getScore_0() {
                return score_0;
            }

            public void setScore_0(int score_0) {
                this.score_0 = score_0;
            }

            public int getScore_5() {
                return score_5;
            }

            public void setScore_5(int score_5) {
                this.score_5 = score_5;
            }

            public int getScore_6() {
                return score_6;
            }

            public void setScore_6(int score_6) {
                this.score_6 = score_6;
            }

            public int getScore_7() {
                return score_7;
            }

            public void setScore_7(int score_7) {
                this.score_7 = score_7;
            }

            public int getScore_8() {
                return score_8;
            }

            public void setScore_8(int score_8) {
                this.score_8 = score_8;
            }

            public int getScore_9() {
                return score_9;
            }

            public void setScore_9(int score_9) {
                this.score_9 = score_9;
            }

            public int getScore_10() {
                return score_10;
            }

            public void setScore_10(int score_10) {
                this.score_10 = score_10;
            }

            public int getAll_count() {
                return all_count;
            }

            public void setAll_count(int all_count) {
                this.all_count = all_count;
            }

            public int getHit_count() {
                return hit_count;
            }

            public void setHit_count(int hit_count) {
                this.hit_count = hit_count;
            }

            public int getHit_score() {
                return hit_score;
            }

            public void setHit_score(int hit_score) {
                this.hit_score = hit_score;
            }


        }
    }
}
