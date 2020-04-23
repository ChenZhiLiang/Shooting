package com.tianfan.shooting.admin.taskdata;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.TaskRankListAdapter;
import com.tianfan.shooting.admin.mvp.presenter.TaskRankPresenter;
import com.tianfan.shooting.admin.mvp.presenter.TaskTeamPresenter;
import com.tianfan.shooting.admin.mvp.view.TaskRankView;
import com.tianfan.shooting.admin.mvp.view.TaskTeamView;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.bean.TaskPersonBean;
import com.tianfan.shooting.bean.TaskRankBean;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.utills.Utils;
import com.tianfan.shooting.view.AddTaskPersonDialog;
import com.tianfan.shooting.view.EditTaskPersonDialog;
import com.tianfan.shooting.view.hrecycler.HRecyclerView;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import me.rosuh.filepicker.config.FilePickerManager;

import static com.tianfan.shooting.admin.taskdata.FraDuiyuan.IMPORT_TASK_PERSON;

/**
 * 队列管理
 * CreateBy：lxf
 * CreateTime： 2020-02-24 11:51
 */
public class FragLieDui extends BaseFragment implements TaskTeamView , TaskRankView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.input_by_excel)
    ImageView input_by_excel;
    @BindView(R.id.iv_init_liedui)
    ImageView iv_init_liedui;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.hrecyclerview)
    HRecyclerView mHrecyclerview;

    private List<TaskRankBean> mTaskRankDatas = new ArrayList<>();

    private String task_id;
    private String task_name;
    private TaskRankPresenter mTaskRankPresenter;
    private TaskTeamPresenter mTaskTeamPresenter;
    private TaskRankListAdapter mTaskRankListAdapter;
    String[] headerListData;
    private int task_person_type = 2;
    private AddTaskPersonDialog mAddTaskPersonDialog;
    private String headUri;//头像url
    private EditTaskPersonDialog mEditTaskPersonDialog;
    public static FragLieDui getInstance(String task_id,String task_name) {
        FragLieDui hf = new FragLieDui();
        hf.task_id = task_id;
        hf.task_name = task_name;
        return hf;
    }

    @Override
    public void loadingData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_lie_dui;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        tv_title.setText(task_name+"-列队管理");
        mTaskTeamPresenter = new TaskTeamPresenter(this);
        mTaskRankPresenter = new TaskRankPresenter(this);
        headerListData = new String[10];
        for (int i = 0; i < 10; i++) {
            headerListData[i] = (i+1)+ "号靶";
        }
        mHrecyclerview.setHeaderListData(headerListData);

    }

    @Override
    public void initData() {
        mTaskTeamPresenter.recordTaskPersonScore(task_id,true);
    }
    private void onRefresh() {
        mTaskTeamPresenter.recordTaskPersonScore(task_id,false);
    }


    @Override
    public void FindTaskPersonResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            JSONArray jsonArray = JSONArray.parseArray(datas);
            mTaskRankDatas.clear();
            for (int i = 0;i<jsonArray.size();i++){
                JSONObject itemsObject = JSONObject.parseObject(jsonArray.get(i).toString());
                String items = itemsObject.getString(String.valueOf(i+1));
                List<TaskPersonBean> mDatas = JSONArray.parseArray(items, TaskPersonBean.class);
                if (mDatas.size() > 0) {
                    //当前最大数
                    int maxPersonCol = mDatas.get(mDatas.size() - 1).getPerson_col();
                    for (int j = 0; j < maxPersonCol; j++) {
                        int config = j + 1;
                        if (mDatas.get(j).getPerson_col() != config) {
                            mDatas.add(j, new TaskPersonBean());
                        }
                    }
                }
                TaskRankBean mTaskRankBean = new TaskRankBean();
                mTaskRankBean.setDatas(mDatas);
                mTaskRankDatas.add(mTaskRankBean);
            }

            mTaskRankListAdapter = new TaskRankListAdapter(mActivity, mTaskRankDatas, new TaskRankListAdapter.OnItemClickListener() {
                @Override
                public void onItemChildClick(int parentPostion, int childPosition) {
                    TaskPersonBean data =  mTaskRankDatas.get(parentPostion).getDatas().get(childPosition);
                    //添加
                    if (data!=null&& TextUtils.isEmpty(data.getTask_id())){
                        int person_row  = parentPostion+1;//行数
                        int person_col  = childPosition + 1;//列数
                        mAddTaskPersonDialog = new AddTaskPersonDialog(mActivity, task_person_type,person_row,person_col, new AddTaskPersonDialog.ClickConfirInterface() {
                            @Override
                            public void onResult(String person_name, String person_idno, String person_orga, String person_role, String person_row, String person_col, String picUri) {
                                headUri = picUri;
                                //查询该分组和靶位是否有人
                                mTaskTeamPresenter.queryTaskPerson(task_id, "", person_row, person_col, new ResultCallback() {
                                    @Override
                                    public void onSuccess(Object result) {
                                        JSONObject jsonObject = JSONObject.parseObject(result.toString());
                                        int code = jsonObject.getIntValue("code");
                                        if (code == 1) {
                                            String datas = jsonObject.getString("datas");
                                            List<TaskPersonBean> mDatas = JSONArray.parseArray(datas, TaskPersonBean.class);
                                            if (mDatas.size()>0){
                                                showLoadFailMsg("该分组的靶位已有人，请重新输入分组或者靶位");
                                            }else {
                                                mTaskTeamPresenter.addTaskPerson(task_id, person_idno, person_name, person_orga, person_role, person_row, person_col);

                                            }
                                        } else if (code==2){
                                            mTaskTeamPresenter.addTaskPerson(task_id, person_idno, person_name, person_orga, person_role, person_row, person_col);

                                        }else {
                                            Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    @Override
                                    public void onFailure(Object result) {
                                        showLoadFailMsg(result.toString());
                                    }
                                });

                            }
                        });
                        mAddTaskPersonDialog.show();
                    }else {//修改
                        mEditTaskPersonDialog = new EditTaskPersonDialog(mActivity, data, mTaskTeamPresenter);
                        mEditTaskPersonDialog.show();
                    }

                }
            });
            mHrecyclerview.setAdapter(mTaskRankListAdapter);
            mTaskRankListAdapter.notifyDataSetChanged();

        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void AddTaskPersonResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            //headUri 为空表示 不用上传头像
            if (TextUtils.isEmpty(headUri)) {
                if (mAddTaskPersonDialog.isShowing()) {
                    mAddTaskPersonDialog.dismiss();
                }
                showLoadFailMsg("添加成功");
                onRefresh();
            } else {
                String data = jsonObject.getString("data");
                TaskPersonBean personBean = JSONObject.parseObject(data, TaskPersonBean.class);
                if (personBean != null) {
                    File file = new File(headUri);
                    if (file.exists()) {
                        mTaskTeamPresenter.uploadTaskPersonHead(personBean.getTask_id(), personBean.getPerson_id(), file);
                    } else {
                        showLoadFailMsg("头像不存在");
                    }
                }
            }

        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void EditTaskPersonResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            if (mEditTaskPersonDialog.isShowing()) {
                mEditTaskPersonDialog.dismiss();
            }
            showLoadFailMsg("修改队员成功");
            onRefresh();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void RemoveTaskPersonResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            if (mEditTaskPersonDialog!=null&&mEditTaskPersonDialog.isShowing()) {
                mEditTaskPersonDialog.dismiss();
            }
            showLoadFailMsg("删除队员成功");
            onRefresh();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void UploadTaskPersonHeadResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            if (mAddTaskPersonDialog != null && mAddTaskPersonDialog.isShowing()) {
                mAddTaskPersonDialog.dismiss();
                showLoadFailMsg("添加成功");
            } else {
                showLoadFailMsg("队员更换头像成功");
            }
            onRefresh();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));

        }
    }

    @Override
    public void ImportTaskPersonGroupResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 0) {
            showLoadFailMsg(jsonObject.getString("message"));
            onRefresh();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));

        }
    }

    @Override
    public void SetTaskPersonStatusResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code==1){
            onRefresh();
        }else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void createTaskPersonRowcolResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            onRefresh();
        }
        showLoadFailMsg(jsonObject.getString("message"));
    }

    @Override
    public void ChangeTaskPersonRowcolResult(Object result) {

    }
    @Override
    public void showProgress() {

        mLoadingDialog.show();
    }

    @Override
    public void hideProgress() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showLoadFailMsg(String err) {

        Toast.makeText(mActivity,err,Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.iv_back,R.id.iv_clear,R.id.iv_init_liedui,R.id.input_by_excel})
    public void onClick(View v) {
        if (v==iv_back){
            getActivity().finish();
        }else if (v==iv_init_liedui){
            //队员数据置入
            SweetAlertDialogTools.ShowDialog(getActivity(), "是否置入队员数据？", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    mTaskRankPresenter.createTaskPersonRowcol(task_id);
                }
            });

        }else if (v==input_by_excel){
            FilePickerManager.INSTANCE.from(this).maxSelectable(1).forResult(IMPORT_TASK_PERSON);

        }else if (v==iv_clear){


            SweetAlertDialogTools.ShowDialog(getActivity(), "是否要清空队列？", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    List<String>personIds = new ArrayList<>();
                    for (int i = 0; i < mTaskRankDatas.size(); i++) {
                        for (int j=0;j<mTaskRankDatas.get(i).getDatas().size();j++){
                            if (!TextUtils.isEmpty(mTaskRankDatas.get(i).getDatas().get(j).getPerson_id())){
                                personIds.add(mTaskRankDatas.get(i).getDatas().get(j).getPerson_id());
                            }
                        }
                    }
                    mTaskTeamPresenter.removeTaskPerson(task_id, Utils.listToString(personIds));
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", "----" + requestCode);
        Log.e("resultCode", "----" + resultCode);
        if (requestCode == IMPORT_TASK_PERSON) {
            if (FilePickerManager.INSTANCE.obtainData().size() > 0) {
                String result = FilePickerManager.INSTANCE.obtainData().get(0);
                File file = new File(result);
                if (file.exists()) {
                    File tempFile = Utils.nioTransferCopy(file);
                    if (tempFile.exists()) {
                        mTaskTeamPresenter.importTaskPersonGroup(task_id, tempFile);
                    }
                }
            }
        }
    }
  /*  @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frag_zl_ld, container, false);
            ButterKnife.bind(this, view);
//            task_row_count 行数据 task_row_persons 列数据
            hang = Integer.parseInt(taskData.getString("task_row_count"));
            lie = Integer.parseInt(taskData.getString("task_row_persons"));
            Log.e("队列数据","---------->"+hang+"---->"+lie);
            tv_title.setText(taskData.getString("task_name")+"-队列管理");
            initView();
            initLisener(view);
        }
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void loadingData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fuckSlect(SelectDLEven event) {
//        Log.e("列队选择回调", ">>>>>>>" + event);
//        //处理逻辑
//        List<JSONObject> list = event.getList();
//        Log.e("列队选择回调Size", ">>>>>>>" + list.size());
//        Log.e("列队选择回调", ">>>>>>>" + event);
//        for (int i = 0; i < list.size(); i++) {
//            jsonArrayForUser.add(list.get(i));
//        }
//        Log.e("列队选择回调", "adapForUserSIze>>>>>>>" + jsonArrayForUser.size());
//        adapForUser.notifyDataSetChanged();
    }
    int hang = 5;
    int lie = 10;
    void initList() {
//        测试数据、生成X 行X列的数据，并对行列数据做相应的处理
        //初始化列队信息、每一行排头的数据，显示添加数据的入口，暂无数据的隐藏不显示
        int allDataCount = hang * lie;
        jsonArrayForUser.clear();
//        for (int i = 0; i < allDataCount; i++) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("p", "位置" + (i + 1));
//            jsonObject.put("data", "");
//            jsonArrayForUser.add(jsonObject);
//        }
//        int fuckCount = 0;
//
//        for (int i=0;i<jsonArrayForUser.size();i++){
//            if (fuckCount==0){
//                jsonArrayForUser.getJSONObject(i).put("viewStatus",2);
//                jsonArrayForUser.getJSONObject(i).put("data","");
//            }else{
//                jsonArrayForUser.getJSONObject(i).put("viewStatus",1);
//                jsonArrayForUser.getJSONObject(i).put("data","");
//            }
//            fuckCount=fuckCount+1;
//            if (fuckCount==lie){
//                fuckCount = 0;
//            }
//        }
        adapForUser.notifyDataSetChanged();
    }

    void createTaskData(){
        HashMap createMap = new HashMap();
        createMap.put("task_id",taskData.getString("task_id"));
        NewNetTools.dobasePost(NewNetTools.BaseURL, NewNetTools.dlCreate, createMap, new NewNetTools.NetCallBack() {
            @Override
            public void netOk(String data) {
                Log.e("","");
                Log.e("列队初始化---",""+data);
                getDLieList();
            }

            @Override
            public void netFail(String msg) {
                Log.e("列队初始化-----netFail",""+msg);
            }
        });
    }

   private void getDLieList(){
        HashMap getListMap = new HashMap();
        getListMap.put("task_id",taskData.getString("task_id"));
        getListMap.put("task_person_type","2");
        NewNetTools.dobasePost(NewNetTools.BaseURL, NewNetTools.dlGetList, getListMap, new NewNetTools.NetCallBack() {
            @Override
            public void netOk(String data) {
                Log.e("列队查询回调-----",""+data);

                JSONObject jsonObject = JSONObject.parseObject(data);
                JSONArray array = jsonObject.getJSONArray("datas");
                jsonArrayForUser.clear();
                for (int i = 0; i < array.size(); i++) {
                    jsonArrayForUser.add(array.get(i));
                }

                adapForUser.notifyDataSetChanged();

//                查不到数据
            }
            @Override
            public void netFail(String msg) {
                Log.e("","");
                Log.e("列队查询-----netFail",""+msg);
                createTaskData();
            }
        });
    }

   private void initView() {

        JSONArray arrayGruo = new JSONArray();
        for (int i = 0; i < hang; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "第" + (i + 1) + "组");
            jsonObject.put("grupNumer", (i + 1) + "");
            arrayGruo.add(jsonObject);

        }
        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewGroup.setAdapter(new AdapForGroup(arrayGruo, getContext()));
        recyclerViewUser.setLayoutManager(new GridLayoutManager(getContext(), lie));
        adapForUser = new AdapForUser(jsonArrayForUser, getContext(), new UserClickCB() {
            // 增加用户
            @Override
            public void addUser(int position) {
                FragLieDui.this.addUser(position);
            }

            //编辑用户
            @Override
            public void edUser(int position) {

            }
        });
        recyclerViewUser.setLongPressDragEnabled(true);
        recyclerViewUser.setAdapter(adapForUser);
//        OnItemMoveListener mItemMoveListener = new OnItemMoveListener() {
//            @Override
//            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
//                int fromPosition = srcHolder.getAdapterPosition();
//                int toPosition = targetHolder.getAdapterPosition();
//                if ((jsonArrayForUser.getJSONObject(fromPosition).getInteger("viewStatus") == 2)) {
//                    return true;
//                }
//
////                if ((jsonArrayForUser.getJSONObject(fromPosition).getInteger("viewStatus") == 3) &
////                        (jsonArrayForUser.getJSONObject(toPosition).getInteger("viewStatus") == 2)) {
////                    Log.e("要移动的数据的信息","-----"+jsonArrayForUser.getJSONObject(fromPosition).getJSONObject("data"));
//////                    调用更改位置接口、接口成功以后再做数据列表的置换
////                    int moveToGroup = getDataGroup(toPosition);
////                    int moveToPosition = getPositionData(toPosition);
////                    Log.e("要移动的位置信息","---moveToGroup--"+moveToGroup+"---moveToPosition-----"+moveToPosition);
////                    HashMap<String,String> map = new HashMap();
////                    map.put("task_id",taskData.getString("task_id"));
////                    map.put("person_id",jsonArrayForUser.getJSONObject(fromPosition).getJSONObject("data").getString("person_id"));
////                    map.put("person_row",moveToGroup+"");
////                    map.put("person_col",""+moveToPosition);
////                    NewNetTools.dobasePost(NewNetTools.BaseURL, NewNetTools.changeTaskPersonRowcol, map, new NewNetTools.NetCallBack() {
////                        @Override
////                        public void netOk(String data) {
////                            Toast.makeText(getContext(),data,Toast.LENGTH_SHORT).show();
////                            JSONObject foJSON = jsonArrayForUser.getJSONObject(fromPosition);
////                            JSONObject toJSON = jsonArrayForUser.getJSONObject(toPosition);
////                            jsonArrayForUser.remove(toPosition);
////                            jsonArrayForUser.add(toPosition, foJSON);
////                            jsonArrayForUser.remove(fromPosition);
////                            jsonArrayForUser.add(fromPosition, toJSON);
////                            reRangeData(fromPosition, new ResetCallBack() {
////                                @Override
////                                public void over() {
////                                    reRangeData(toPosition);
////                                }
////                            });
////                        }
////                        @Override
////                        public void netFail(String msg) {
////                            Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
////                        }
////                    });
////                }
//                return true;
//            }
//
//            @Override
//            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
//            }
//
//        };
//        recyclerViewUser.setOnItemMoveListener(mItemMoveListener);
        initList();
        getDLieList();
    }

    private void addUser(int position){
//        显示增加队员对话框
        dialogAddUser = new DialogAddUser(getContext(), new FraDuiyuan.AddUserCB() {
            @Override
            public void cbData(JSONObject jsonObject) {
                Log.e("测试----","----"+jsonObject);
                String idNmner = jsonObject.getString("ed_id_number");
                HashMap<String, String> map = new HashMap<>();
                map.put("task_id", taskData.getString("task_id"));
                map.put("person_idno", idNmner);
                map.put("person_name", jsonObject.getString("ed_name"));
                map.put("person_orga", jsonObject.getString("ed_work_unit"));
                map.put("person_role", jsonObject.getString("ed_user_type"));
                map.put("person_row", jsonObject.getString("ed_user_group"));
                map.put("person_col", jsonObject.getString("ed_user_positon"));
                map.put("task_person_type", "2");
                NewNetTools.dobasePost(NewNetTools.BaseURL, NewNetTools.addUser, map, new NewNetTools.NetCallBack() {
                    @Override
                    public void netOk(String data) {
                        Log.e("队员增加回调--", "---" + data);
                        jsonArrayForUser.getJSONObject(position).put("viewStatus", 3);
                        jsonArrayForUser.getJSONObject(position).put("p",jsonObject.getString("ed_name"));
                        JSONObject addJSON = JSONObject.parseObject(data);
                        jsonArrayForUser.getJSONObject(position).put("data",addJSON.getJSONObject("data"));
                        reRangeData(position);
                        JSONObject rspJSON = JSONObject.parseObject(data);
                        Toast.makeText(getContext(), rspJSON.getString("message"), Toast.LENGTH_SHORT).show();
                        if (!jsonObject.getString("ed_user_pic").equals("")) {
                            upUserIcon(jsonObject.getString("ed_user_pic"), rspJSON.getJSONObject("data").getString("person_id"));
                        }
                    }
                    @Override
                    public void netFail(String msg) {
                        Log.e("队员增加回调netFail--", "---" + msg);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        },getDataGroup(position),getPositionData(position));
        dialogAddUser.show();

    }

    void upUserIcon(String fileURI, String psersonId) {
        Log.e("upUserIcon", "--psersonId--" + psersonId);
        Log.e("upUserIcon", "--fileURI--" + fileURI);
        File file = new File(fileURI);
        HashMap<String, String> map = new HashMap<>();
        map.put("task_id", taskData.getString("task_id"));
        map.put("person_id", psersonId);
        map.put("task_person_type", "2");
        File tempFile = NewNetTools.nioTransferCopy(file);
        NewNetTools.doBaseUpFile(NewNetTools.BaseURL + NewNetTools.upUserIcon, tempFile, map, new NewNetTools.NetCallBack() {
            @Override
            public void netOk(String data) {
                Log.e("队员头像上传回调", "----" + data);
//                initData();
                tempFile.delete();
                Toast.makeText(getContext(), "" + JSONObject.parseObject(data).getString("message"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netFail(String msg) {
                Log.e("队员头像上传回调netFail", "----" + msg);
                Toast.makeText(getContext(), "--" + msg, Toast.LENGTH_SHORT).show();
                tempFile.delete();
//                initData();
            }
        });
    }

    DialogAddUser dialogAddUser;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }


    class DialogAddUser extends Dialog {
        public ImageView imageView;
        private String picURI = "";
        private int gropuCount =1;
        private int positionCount =1;
        private String userType = "射击员";
        private List<String> spStr = new ArrayList<>();
        String[] IDCARD = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "x", "X", };
        final List<String> idCardList = Arrays.asList(IDCARD);

        public DialogAddUser(@NonNull Context context, FraDuiyuan.AddUserCB createCallBack,int GrpuLimit,int positionLimit) {
            super(context);
            setContentView(R.layout.dialog_add_user);
            getWindow().setGravity(Gravity.TOP);
            spStr.add("射击员");
            spStr.add("管理员");
            spStr.add("记分员");

            MaterialSpinner spinner = findViewById(R.id.sp_user_type);
            spinner.setText("请选择");
            spinner.setItems(spStr);
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    if (position==0){
                        userType = "射击员";
                    }
                    if (position==1){
                        userType = "管理员";
                    }
                    if (position==2){
                        userType = "记分员";
                    }
                }
            });

            findViewById(R.id.id_cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

//            getWindow().findViewById(R.id.iv_gropu_down).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (gropuCount>=2){
//                        gropuCount =gropuCount-1;
//                        ((TextView) findViewById(R.id.tv_group)).setText(""+gropuCount);
//                    }
//                }
//            });
//
//            getWindow().findViewById(R.id.iv_gropu_up).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (gropuCount<=hang){
//                        if (gropuCount!=hang){
//                            gropuCount =gropuCount+1;
//                        }
//                        ((TextView) findViewById(R.id.tv_group)).setText(""+GrpuLimit);
//                    }
//                }
//            });
            getWindow().findViewById(R.id.iv_gropu_up).setVisibility(View.GONE);
            getWindow().findViewById(R.id.iv_gropu_down).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_group)).setText(""+GrpuLimit);
            ((TextView) findViewById(R.id.tv_position)).setText(""+positionLimit);
            getWindow().findViewById(R.id.iv_position_down).setVisibility(View.GONE);
            getWindow().findViewById(R.id.iv_position_up).setVisibility(View.GONE);
            imageView = findViewById(R.id.iv_user_icon);
            findViewById(R.id.iv_user_icon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureSelector.create(getActivity())
                            .openGallery(PictureMimeType.ofImage())
                            .maxSelectNum(1)
                            .loadImageEngine(GlideEngine.createGlideEngine())
//                            / 请参考Demo GlideEngine.java
                            .forResult(new OnResultCallbackListener() {
                                @Override
                                public void onResult(List<LocalMedia> result) {
                                    if (result.size() > 0) {
                                        LocalMedia media = result.get(0);
                                        picURI = media.getPath();
                                        File file = new File(picURI);
                                        RequestOptions options = new RequestOptions().error(R.drawable.user_icon).bitmapTransform(new RoundedCorners(30));//图片圆角为30

                                        Glide.with(getContext()).load(Uri.fromFile(file)).override(120, 120).apply(options).into(imageView);
                                    }
                                }

                                @Override
                                public void onCancel() {
                                }
                            })
                    ;
                }
            });
            findViewById(R.id.tv_takepic_user_icon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureSelector.create(getActivity())
                            .openGallery(PictureMimeType.ofImage())
                            .maxSelectNum(1)
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .forResult(new OnResultCallbackListener() {
                                @Override
                                public void onResult(List<LocalMedia> result) {
                                    if (result.size() > 0) {
                                        LocalMedia media = result.get(0);
                                        picURI = media.getPath();
                                        File file = new File(picURI);
                                        RequestOptions options = new RequestOptions().error(R.drawable.user_icon).bitmapTransform(new RoundedCorners(30));//图片圆角为30
                                        Glide.with(getContext()).load(Uri.fromFile(file)).override(120, 120).apply(options).into(imageView);
                                    }
                                }

                                @Override
                                public void onCancel() {
                                }
                            })
                    ;
//                    组别加减的监听
                }
            });

            InputFilter inputFilter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    // 返回空字符串，就代表匹配不成功，返回null代表匹配成功
                    for (int i = 0; i < source.toString().length(); i++) {
                        if (!idCardList.contains(String.valueOf(source.charAt(i)))) {
                            return "";
                        }
                        if (((EditText) findViewById(R.id.ed_id_number)).getText().toString().length() < 17) {
                            if ("x".equals(String.valueOf(source.charAt(i))) || "X".equals(String.valueOf(source.charAt(i)))) {
                                return "";
                            }
                        }
                    }
                    return null;
                }
            };
            ((EditText) findViewById(R.id.ed_id_number)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(18), inputFilter});
            findViewById(R.id.comfir).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((EditText) findViewById(R.id.ed_name)).getText().toString().equals("")) {
                        Toast.makeText(getContext(), "请输入队员名称", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (((EditText) findViewById(R.id.ed_id_number)).getText().toString().equals("")) {
                        Toast.makeText(getContext(), "请输入身份证号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (((EditText) findViewById(R.id.ed_work_unit)).getText().toString().equals("请选择")) {
                        Toast.makeText(getContext(), "请输入工作单位", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    if (((EditText) findViewById(R.id.ed_user_type)).getText().toString().equals("")) {
//                        Toast.makeText(getContext(), "请输入角色类型", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    if (((TextView) findViewById(R.id.tv_group)).getText().toString().equals("0")) {
                        Toast.makeText(getContext(), "分组位置不能为0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (((TextView) findViewById(R.id.tv_position)).getText().toString().equals("0")) {
                        Toast.makeText(getContext(), "队员位置不能为0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("ed_name", ((EditText) findViewById(R.id.ed_name)).getText().toString());
                    jsonObject.put("ed_id_number", ((EditText) findViewById(R.id.ed_id_number)).getText().toString());
                    jsonObject.put("ed_work_unit", ((EditText) findViewById(R.id.ed_work_unit)).getText().toString());
                    jsonObject.put("ed_user_type", userType);
                    jsonObject.put("ed_user_group", ((TextView) findViewById(R.id.tv_group)).getText().toString());
                    jsonObject.put("ed_user_positon", ((TextView)findViewById(R.id.tv_position)).getText().toString());
                    jsonObject.put("ed_user_pic", picURI);
                    createCallBack.cbData(jsonObject);
                    dismiss();
                }
            });
//            ((EditText) findViewById(R.id.ed_name)).setText(dialogJSON.getString("ed_name"));
//            ((EditText) findViewById(R.id.ed_id_number)).setText(dialogJSON.getString("ed_id_number"));
//            ((EditText) findViewById(R.id.ed_work_unit)).setText(dialogJSON.getString("ed_work_unit"));
////            ((EditText) findViewById(R.id.ed_user_type)).setText(dialogJSON.getString("ed_user_type"));
//            ((TextView) findViewById(R.id.tv_group)).setText(dialogJSON.getString("ed_user_group"));
//            ((TextView) findViewById(R.id.tv_position)).setText(dialogJSON.getString("ed_user_positon"));
        }
    }


    void saveData() {
        if (jsonArrayForUser.size() < 1) {
            Toast.makeText(getContext(), "请至少添加一个排列", Toast.LENGTH_SHORT).show();
        } else {

            sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitleText("提示");
            sweetAlertDialog.setContentText("是否保存?");
            sweetAlertDialog.setCancelText("取消");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Map map = new HashMap();
                    map.put("data", JSONObject.toJSONString(jsonArrayForUser));
                    RequestTools.doAction().getData(RetrofitUtils.getService().saveRange(map),
                            new GetResult<String>() {
                                @Override
                                public void fail(String msg) {
                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void ok(String s) {
                                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                                }
                            });
                    sweetAlertDialog.dismiss();
                }
            });
            sweetAlertDialog.show();
        }
    }

    void initLisener(View view) {
        view.findViewById(R.id.tv_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (jsonArrayForUser.size() > 0) {
//                    Intent intent = new Intent();
//                    intent.setClass(getContext(), AdminActivity.class);
//                    startActivity(intent);
////                    clearList();
//                }
                Intent intent = new Intent();
                intent.setClass(getContext(), AdminActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        view.findViewById(R.id.iv_init_liedui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                队员数据置入
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setCancelText("取消")
                        .setConfirmText("是")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                createTaskData();
                                sweetAlertDialog.dismiss();
                            }
                        }).setTitleText("提示")
                        .setContentText("是否置入队员数据？")
                        .show();
            }
        });
        view.findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setCancelText("取消")
                        .setConfirmText("是")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                createTaskData();
                                sweetAlertDialog.dismiss();
                            }
                        }).setTitleText("提示")
                        .setContentText("是否清空队员数据？")
                        .show();
            }
        });

    }


    private int getDataGroup(int changeP){
        int cTest = 0;
        Log.e("所处组别计算","---"+cTest);
        List<List<List>> allList = new ArrayList<>();
        for (int i = 0; i < hang; i++) {
            List<List> zuList = new ArrayList();
            for (int j = 0; j < lie; j++) {
                List fucList = new ArrayList();
                fucList.add("position");
                fucList.add("data");
                zuList.add(fucList);
            }
            allList.add(zuList);
        }
        for (int i = 0; i < allList.size(); i++) {
            for (int j = 0; j < allList.get(i).size(); j++) {
//              算出j现在相对于队员列表中的哪个位置
                if (i == 0) {
                    int position = j;
                    allList.get(i).get(j).remove(1);
                    allList.get(i).get(j).add(jsonArrayForUser.get(position));
                    if (position==changeP){
                        cTest = i;
                    }
                } else {
                    int position = (lie * i) + j;
                    allList.get(i).get(j).remove(1);
                    allList.get(i).get(j).add(jsonArrayForUser.get(position));
                    if (position==changeP){
                        cTest = i;
                    }
                }
            }
        }
        return cTest +1;
    }

    private int getPositionData(int changeP){
        int result = 0;
        for (int i = 0; i < jsonArrayForUser.size(); i++) {
            result = result+1;
            if (result==lie){
                result = 0;
            }
            if (changeP==i){
                break;
            }
        }
        return result ;
    }


    void clearList() {
        sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.BUTTON_CONFIRM);
        sweetAlertDialog.setTitle("提示");
        sweetAlertDialog.setContentText("是否清除当前队列信息？");
        sweetAlertDialog.setCancelText("取消");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                jsonArrayForUser.clear();
                adapForUser.notifyDataSetChanged();
                RequestTools.doAction().getData(RetrofitUtils.getService().clearData(new HashMap<>()),
                        new GetResult<String>() {
                            @Override
                            public void fail(String msg) {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void ok(String s) {
                                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        sweetAlertDialog.show();
    }

    interface UserClickCB {
        void addUser(int position);

        void edUser(int position);
    }

    class AdapForUser extends RecyclerView.Adapter {
        JSONArray jsonArray;
        Context context;
        UserClickCB cb;

        public AdapForUser(JSONArray jsonArray, Context context, UserClickCB userClickCB) {
            this.jsonArray = jsonArray;
            this.context = context;
            this.cb = userClickCB;

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_chil, parent, false);
            VHListChil vhListChil = new VHListChil(view);
            return vhListChil;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof VHListChil) {
                holder.setIsRecyclable(false);
//                int type = jsonArrayForUser.getJSONObject(position).getInteger("viewStatus");
//                if (jsonArrayForUser.getJSONObject(position).getInteger("viewStatus") == 1) {
//                    ((VHListChil) holder).itemView.setVisibility(View.GONE);
//                }
//                if (jsonArrayForUser.getJSONObject(position).getInteger("viewStatus") == 2) {
//                    ((ImageView) holder.itemView.findViewById(R.id.iv)).setImageResource(R.drawable.add_icon_wihte_mini);
//                    holder.itemView.findViewById(R.id.lv).setBackgroundResource(R.drawable.view_liedui_user_grenn_bg);
//                    holder.itemView.findViewById(R.id.tv_user_name).setVisibility(View.VISIBLE);
//                }
//                if (jsonArrayForUser.getJSONObject(position).getInteger("viewStatus") == 3) {
                    ((VHListChil) holder).title.setText(jsonArray.getJSONObject(position).getString("person_name"));
//                    ((ImageView) holder.itemView.findViewById(R.id.iv)).setImageResource(R.drawable.user_icon);
//                    holder.itemView.findViewById(R.id.lv).setBackgroundResource(R.drawable.view_liedui_user_normal_bg);
//                }
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.e("click", "--->" + holder.getAdapterPosition());
//                        if (type == 2) {
//                            cb.addUser(position);
//                        }
//                        if (type == 3) {
//                            cb.edUser(position);
//                        }
//                    }
//                });
            }
        }

        @Override
        public int getItemCount() {
            return jsonArray.size();
        }
    }


//    处理拖拽事件和增加事件后数据的重新排列

    *//**
     * 处理思路，一组一组的处理，然后，算了看不懂就算了
     *//*

    interface ResetCallBack{
        void over();
    }


    public void reRangeData(int changeP){
        int cTest = 0;
        Log.e("所处组别计算","---"+cTest);
        List<List<List>> allList = new ArrayList<>();
        for (int i = 0; i < hang; i++) {
            List<List> zuList = new ArrayList();
            for (int j = 0; j < lie; j++) {
                List fucList = new ArrayList();
                fucList.add("position");
                fucList.add("data");
                zuList.add(fucList);
            }
            allList.add(zuList);
        }
        for (int i = 0; i < allList.size(); i++) {
            for (int j = 0; j < allList.get(i).size(); j++) {
//              算出j现在相对于队员列表中的哪个位置
                if (i == 0) {
                    int position = j;
                    allList.get(i).get(j).remove(1);
                    allList.get(i).get(j).add(jsonArrayForUser.get(position));
                    if (position==changeP){
                        cTest = i;
                    }
                } else {
                    int position = (lie * i) + j;
                    allList.get(i).get(j).remove(1);
                    allList.get(i).get(j).add(jsonArrayForUser.get(position));
                    if (position==changeP){
                        cTest = i;
                    }
                }
            }
        }
//        开始判断数据的插入和改变了，有数据的排头，没数据的置换到队列后面
        for (int j = 0; j < allList.get(cTest).size(); j++) {
            for (int m = 0; m < allList.get(cTest).size(); m++) {
                //取出组内数据、对组内数据大小判断、排序
                if (m != allList.get(cTest).size() - 1) {
                    List tmp = allList.get(cTest).get(m);
                    JSONObject j1 = (JSONObject) tmp.get(1);
                    List tmpNext = allList.get(cTest).get(m + 1);
                    JSONObject j2 = (JSONObject) tmpNext.get(1);
                    if (j1.getInteger("viewStatus") < j2.getInteger("viewStatus")) {
                        allList.get(cTest).set(m, tmpNext);
                        allList.get(cTest).set(m + 1, tmp);
                    }
                }
            }
        }
        Log.e("处理","第"+(cTest+1)+"组处理");
        boolean testFlag = false;
        boolean newFlgs = false;
        for (int m=0;m<allList.get(cTest).size();m++){
            if (testFlag){
                break;
            }
            if (newFlgs){
                break;
            }
            if (!testFlag){
                if (((JSONObject) allList.get(cTest).get(m).get(1)).getInteger("viewStatus")==1){
                    ((JSONObject) allList.get(cTest).get(m).get(1)).put("viewStatus",2);
                    testFlag = true;
//                        后面的全部替换为1
                    for (int j=m;j<allList.get(cTest).size()-1;j++){
                        ((JSONObject) allList.get(cTest).get(j+1).get(1)).put("viewStatus",1);
                    }
                }
            }
            if (testFlag){
                break;
            }
            if (!newFlgs){
                if (((JSONObject) allList.get(cTest).get(m).get(1)).getInteger("viewStatus")==2){
                    newFlgs = true;
//                        后面的全部替换为1
                    for (int j=m;j<allList.get(cTest).size()-1;j++){
                        ((JSONObject) allList.get(cTest).get(j+1).get(1)).put("viewStatus",1);
                    }
                }
            }
            if (newFlgs){
                break;
            }
        }
        for (int i = 0; i < allList.size(); i++) {
            for (int j = 0; j < allList.get(i).size(); j++) {
//              算出j现在相对于队员列表中的哪个位置
                if (i == 0) {
                    int position = j;
                    jsonArrayForUser.set(position, allList.get(i).get(j).get(1));
                } else {
                    int position = (lie * i) + j;
                    jsonArrayForUser.set(position, allList.get(i).get(j).get(1));
                }
            }
        }
        adapForUser.notifyDataSetChanged();
    }

    public void reRangeData(int changeP,ResetCallBack resetCallBack) {
        int cTest = 0;
        List<List<List>> allList = new ArrayList<>();
        for (int i = 0; i < hang; i++) {
            List<List> zuList = new ArrayList();
            for (int j = 0; j < lie; j++) {
                List fucList = new ArrayList();
                fucList.add("position");
                fucList.add("data");
                zuList.add(fucList);
            }
            allList.add(zuList);
        }
        for (int i = 0; i < allList.size(); i++) {
            for (int j = 0; j < allList.get(i).size(); j++) {
//              算出j现在相对于队员列表中的哪个位置
                if (i == 0) {
                    int position = j;
                    allList.get(i).get(j).remove(1);
                    allList.get(i).get(j).add(jsonArrayForUser.get(position));
                    if (position==changeP){
                        cTest = i;
                    }
                } else {
                    int position = (lie * i) + j;
                    allList.get(i).get(j).remove(1);
                    allList.get(i).get(j).add(jsonArrayForUser.get(position));
                    if (position==changeP){
                        cTest = i;
                    }
                }
            }
        }
        Log.e("-----cTest", cTest+"");
//        开始判断数据的插入和改变了，有数据的排头，没数据的置换到队列后面
        for (int j = 0; j < allList.get(cTest).size(); j++) {
            for (int m = 0; m < allList.get(cTest).size(); m++) {
                //取出组内数据、对组内数据大小判断、排序
                if (m != allList.get(cTest).size() - 1) {
                    List tmp = allList.get(cTest).get(m);
                    JSONObject j1 = (JSONObject) tmp.get(1);
                    List tmpNext = allList.get(cTest).get(m + 1);
                    JSONObject j2 = (JSONObject) tmpNext.get(1);
                    if (j1.getInteger("viewStatus") < j2.getInteger("viewStatus")) {
                        allList.get(cTest).set(m, tmpNext);
                        allList.get(cTest).set(m + 1, tmp);
                    }
                }
            }
        }
        Log.e("处理","第"+(cTest+1)+"组处理");
        boolean testFlag = false;
        boolean newFlgs = false;
        for (int m=0;m<allList.get(cTest).size();m++){
            if (testFlag){
                break;
            }
            if (newFlgs){
                break;
            }
            if (m<allList.get(cTest).size()-1){
                if (!testFlag){
                    if (((JSONObject) allList.get(cTest).get(m).get(1)).getInteger("viewStatus")==1){
                        ((JSONObject) allList.get(cTest).get(m).get(1)).put("viewStatus",2);
                        testFlag = true;
//                        后面的全部替换为1
                        for (int j=m;j<allList.get(cTest).size()-1;j++){
                            ((JSONObject) allList.get(cTest).get(j+1).get(1)).put("viewStatus",1);
                        }
                    }
                }
                if (testFlag){
                    break;
                }
                if (!newFlgs){
                    if (((JSONObject) allList.get(cTest).get(m).get(1)).getInteger("viewStatus")==2){
                        newFlgs = true;
//                        后面的全部替换为1
                        for (int j=m;j<allList.get(cTest).size()-1;j++){
                            ((JSONObject) allList.get(cTest).get(j+1).get(1)).put("viewStatus",1);
                        }
                    }
                }
                if (newFlgs){
                    break;
                }
            }
        }
        for (int i = 0; i < allList.size(); i++) {
            for (int j = 0; j < allList.get(i).size(); j++) {
//              算出j现在相对于队员列表中的哪个位置
                if (i == 0) {
                    int position = j;
                    jsonArrayForUser.set(position, allList.get(i).get(j).get(1));
                } else {
                    int position = (lie * i) + j;
                    jsonArrayForUser.set(position, allList.get(i).get(j).get(1));
                }
            }
        }
      resetCallBack.over();
    }
    class DialogShowUser extends Dialog {
        JSONObject jsonObject;
        SelectUserActivity.UserSignI userSignI;

        public DialogShowUser(@NonNull Context context, JSONObject jsonObject, SelectUserActivity.UserSignI userSignI) {
            super(context);
            this.jsonObject = jsonObject;
            this.userSignI = userSignI;
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.item_dianlog_show_user);
            TextView name = findViewById(R.id.tv_name);
            name.setText("姓名:" + jsonObject.getString("user_name"));
            TextView tv_post = findViewById(R.id.tv_post);
            tv_post.setText("职务：" + jsonObject.getString("zhiwu"));
            TextView tv_idNumber = findViewById(R.id.tv_idNumber);
            tv_idNumber.setText("身份证号：" + jsonObject.getString("user_id_card"));
            TextView tv_tell = findViewById(R.id.tv_tell);
            tv_tell.setText("联系电话：" + jsonObject.getString("tell"));
            TextView tv_mtservice = findViewById(R.id.tv_mtservice);
            tv_mtservice.setText("服兵役情况：" + jsonObject.getString("bingyi"));
            TextView tv_work_unit = findViewById(R.id.tv_work_unit);
            tv_work_unit.setText("工作单位：" + jsonObject.getString("work_unit"));
            TextView tv_remark = findViewById(R.id.tv_remark);
            tv_remark.setText("备注：" + jsonObject.getString("remark"));
            TextView tv_is_come = findViewById(R.id.tv_is_come);
            TextView tv_not_come = findViewById(R.id.tv_not_come);
            tv_is_come.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userSignI.result(true);
                    dismiss();
                }
            });
            tv_not_come.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userSignI.result(false);
                    dismiss();
                }
            });


        }
    }


    class AdapForGroup extends RecyclerView.Adapter {
        JSONArray jsonArray;
        Context context;

        public AdapForGroup(JSONArray jsonArray, Context context) {
            this.jsonArray = jsonArray;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
            VHListChil vhListChil = new VHListChil(view);
            return vhListChil;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof VHListChil) {
                ((VHListChil) holder).title.setText(jsonArray.getJSONObject(position).getString("name"));
                ((VHListChil) holder).number.setText(jsonArray.getJSONObject(position).getString("grupNumer"));
            }
        }

        @Override
        public int getItemCount() {
            return jsonArray.size();
        }
    }

    class VHListChil extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView number;

        public VHListChil(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_user_name);
            number = itemView.findViewById(R.id.gr_id);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }*/
}
