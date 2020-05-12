package com.tianfan.shooting.admin.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.ui.fragment.CheckFragment;
import com.tianfan.shooting.admin.ui.fragment.HistoryFragment;
import com.tianfan.shooting.admin.ui.fragment.HomeFragment;
import com.tianfan.shooting.admin.ui.fragment.SetUpFragment;
import com.tianfan.shooting.admin.ui.mdziliao.ZiLiaoMD;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;



/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-02 17:12
 * @Description 主页面
 */
public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.comander_bottom_navigation)
    PageNavigationView pageNavigationView;

    private FragmentTransaction transaction;
    private HomeFragment homeFragment;
//    private FrgForUnDoList frgForUnDoList;
    private HistoryFragment historyFragment;
    private CheckFragment checkFragment;
    private SetUpFragment setUpFragment;
    private Fragment mFragment;

    AppCompatActivity activity;

    @BindView(R.id.iv_shoot)
    Button iv_shoot;

    @BindView(R.id.iv_ziliao)
    Button iv_ziliao;

    @BindView(R.id.iv_zhihui)
    Button iv_zhihui;

    @BindView(R.id.iv_fenxi)
    Button iv_fenxi;

    @BindView(R.id.iv_setup)
    Button iv_setup;

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cammand_main);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
    }

    //点击事件监听
    @OnClick({R.id.iv_shoot,R.id.iv_ziliao
            ,R.id.iv_zhihui,R.id.iv_fenxi,R.id.iv_setup})
    @Override
    public void onClick(View v){
        Intent intent = new Intent();

        switch (v.getId()){
            case R.id.iv_shoot:
                intent.putExtra("type","shoot");
                intent.setClass(getApplicationContext(), ZiliaoActivity.class);

                break;
            case R.id.iv_ziliao:
                intent.putExtra("type","ziliao");
                intent.setClass(getApplicationContext(), ZiLiaoMD.class);

                break;
            case R.id.iv_zhihui:
//                intent.putExtra("type","zhihui");
//                intent.setClass(getApplicationContext(), ZiliaoActivity.class);
                intent.setClass(getApplicationContext(), CommandManageActivity.class);


                break;
            case R.id.iv_fenxi:
                intent.putExtra("type","fenxi");
                intent.setClass(getApplicationContext(), ZiliaoActivity.class);

                break;
            case R.id.iv_setup:
//                intent.putExtra("type","setup");
//                intent.setClass(getApplicationContext(), ZiliaoActivity.class);
                intent.setClass(getApplicationContext(), SystemManageActivity.class);
                break;
        }

        startActivity(intent);
    }

    int count = 0;
    Disposable disposable;

   /* private void doFuckChange(ImageView imageView,Intent intent) {
        count=0;
        disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (imageView ==iv_shoot){
                            Glide.with(getApplicationContext()).load(R.drawable.home_shoot_lig).into(imageView);
                        }else if (imageView==iv_ziliao){
                            Glide.with(getApplicationContext()).load(R.drawable.icon_renwu_highlit).into(imageView);
                        }else if (imageView==iv_zhihui){
                            Glide.with(getApplicationContext()).load(R.drawable.home_zhihui_higliht).into(imageView);
                        }else if (imageView==iv_fenxi){
                            Glide.with(getApplicationContext()).load(R.drawable.home_fenxi_hilight).into(imageView);
                        }else if (imageView==iv_setup){
                            Glide.with(getApplicationContext()).load(R.drawable.home_setup_higlight).into(imageView);
//                            imageView.setBackground();
                        }
                        count = count + 1;
                        if (count==2){
                            startActivity(intent);
                        }
                        if (count==4){
                            if (imageView ==iv_shoot){
                                Glide.with(getApplicationContext()).load(R.drawable.home_shoot).into(imageView);
                            }else if (imageView==iv_ziliao){
                                Glide.with(getApplicationContext()).load(R.drawable.icon_renwu).into(imageView);
                            }else if (imageView==iv_zhihui){
                                Glide.with(getApplicationContext()).load(R.drawable.home_zhihui).into(imageView);
                            }else if (imageView==iv_fenxi){
                                Glide.with(getApplicationContext()).load(R.drawable.home_fenxi).into(imageView);
                            }else if (imageView==iv_setup){
                                Glide.with(getApplicationContext()).load(R.drawable.home_setup).into(imageView);
                            }
                            disposable.dispose();
                        }
                    }
                });
    }*/

    private void showFragment(int page) {
        FragmentTransaction ft = fragmentManager.beginTransaction();

        // 想要显示一个fragment,先隐藏所有fragment，防止重叠
        hideFragments(ft);
        switch (page) {
            case 0:
                // 如果fragment1已经存在则将其显示出来
                if (homeFragment != null)
                    ft.show(homeFragment);
                    // 否则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
                else {
                    homeFragment = new HomeFragment();
                    ft.add(R.id.comander_frag_layout, homeFragment);
                }
                break;
            case 1:
                if (historyFragment != null)
                    ft.show(historyFragment);
                else {
                    historyFragment = new HistoryFragment();
                    ft.add(R.id.comander_frag_layout, historyFragment);
                }
                break;
            case 2:
                if (checkFragment != null) {
                    ft.show(checkFragment);
                }
                else {
                    checkFragment = new CheckFragment();
                    ft.add(R.id.comander_frag_layout, checkFragment);
                }
                break;
            case 3:
                if (setUpFragment != null){
                    ft.show(setUpFragment);
                }
                else {
                    setUpFragment = new SetUpFragment();
                    ft.add(R.id.comander_frag_layout, setUpFragment);
                }
                break;
        }
        ft.commit();
    }

    public void hideFragments(FragmentTransaction ft) {
        if (homeFragment != null)
            ft.hide(homeFragment);
        if (historyFragment != null)
            ft.hide(historyFragment);
        if (checkFragment != null)
            ft.hide(checkFragment);
        if (setUpFragment != null)
            ft.hide(setUpFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private FragmentManager fragmentManager;
}