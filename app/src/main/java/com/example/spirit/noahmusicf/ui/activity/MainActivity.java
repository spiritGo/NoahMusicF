package com.example.spirit.noahmusicf.ui.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.spirit.noahmusicf.R;
import com.example.spirit.noahmusicf.adapter.MenuAdapter;
import com.example.spirit.noahmusicf.modle.ArrayData;
import com.example.spirit.noahmusicf.modle.MenuListBean;
import com.example.spirit.noahmusicf.utils.SPUtil;
import com.example.spirit.noahmusicf.utils.Util;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.ArrayList;

public class MainActivity extends SlidingFragmentActivity {

    private ImageView ivMenu;
    private TextView tvTitle;
    private SlidingMenu slidingMenu;
    private ListView lvMenuList;
    private ArrayList<MenuListBean> menuListBeanList;
    private FragmentManager fragmentManager;
    private RelativeLayout rlTitle;
    private SlidingMenu.CanvasTransformer mTransformer;
    private MenuAdapter menuAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu_layout);
        initView();
        initUI();
    }

    private void initUI() {
        rlTitle.setBackgroundColor(getResources().getColor(SPUtil.getInt("titleColor", R.color
                .blue)));

        initAnimation();

        slidingMenuSet();
        menuListBeanList = ArrayData.getMenuListBeanList();
        menuAdapter = new MenuAdapter(menuListBeanList, this);
        View header = View.inflate(this, R.layout.sliding_menu_list_header, null);
        lvMenuList.addHeaderView(header);
        lvMenuList.setAdapter(menuAdapter);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_container, ArrayData
                .getMenuListTextAndFragment().get(menuListBeanList.get(0).getTitle())).commit();
        setTvTitle(menuListBeanList.get(0).getTitle());

        clickListeners();
    }

    private void clickListeners() {
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        });

        lvMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String title = menuListBeanList.get(position-1).getTitle();
                    setTvTitle(title);
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.alpha_in, R.anim.translate_out)
                            .replace(R.id.fl_container, ArrayData.getMenuListTextAndFragment().get
                                    (title)).commit();

                    menuAdapter.setItem(position-1);
                    menuAdapter.notifyDataSetChanged();

                    slidingMenu.toggle();
                }
            }
        });
    }

    public MenuAdapter getMenuAdapter() {
        return menuAdapter;
    }

    private void slidingMenuSet() {
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffset(Util.dp2px(100));
        slidingMenu.setFadeEnabled(true);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.menu_shadow);
        slidingMenu.setBehindCanvasTransformer(mTransformer);
    }

    private void initAnimation() {
        mTransformer = new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
            }

        };
    }

    private void initView() {
        ivMenu = (ImageView) findViewById(R.id.iv_menu);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        slidingMenu = getSlidingMenu();
        lvMenuList = slidingMenu.findViewById(R.id.lv_menuList);
        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
    }

    public RelativeLayout getRlTitle() {
        return rlTitle;
    }

    private void setTvTitle(String title) {
        tvTitle.setText(title);
    }
}
