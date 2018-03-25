package com.marchsoft.organization;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.marchsoft.organization.fragment.DynamicFragment;
import com.marchsoft.organization.fragment.MainFragment;
import com.marchsoft.organization.fragment.MineFragment;
import com.marchsoft.organization.fragment.OrganizationFragment;
import com.marchsoft.organization.utils.ToastUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateAgent;
public class MainActivity extends BaseActivity {
    private static final String FRAGMENT_TAG_MAIN = "main";
    private static final String FRAGMENT_TAG_ORGANIZATION = "organization";
    private static final String FRAGMENT_TAG_DYNAMIC = "dynamic";
    private static final String FRAGMENT_TAG_MINE = "mine";
    private MainFragment mMainFragment;
    private OrganizationFragment mOrganizationFragment;
    private DynamicFragment mDynamicFragment;
    private MineFragment mMineFragment;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private View mCurTabItemView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getFragmentManager();
        if (savedInstanceState != null){
            mMainFragment = (MainFragment)mFragmentManager.findFragmentByTag(FRAGMENT_TAG_MAIN);
            mOrganizationFragment = (OrganizationFragment) mFragmentManager.findFragmentByTag(FRAGMENT_TAG_ORGANIZATION);
            mDynamicFragment = (DynamicFragment) mFragmentManager.findFragmentByTag(FRAGMENT_TAG_DYNAMIC);
            mMineFragment = (MineFragment) mFragmentManager.findFragmentByTag(FRAGMENT_TAG_MINE);
            FragmentTransaction fg = mFragmentManager.beginTransaction();
            if(mMainFragment != null){
                if (mMainFragment.isAdded()){
                    fg.hide(mMainFragment);
                }
            }
            if (mOrganizationFragment != null){
                if (mOrganizationFragment.isAdded()){
                    fg.hide(mOrganizationFragment);
                }
            }
            if (mDynamicFragment != null){
                if (mDynamicFragment.isAdded()){
                    fg.hide(mDynamicFragment);
                }
            }
            if (mMineFragment != null){
                if (mMineFragment.isAdded()){
                    fg.hide(mMineFragment);
                }
            }
            fg.commit();
        }

        onTabItemClick(findViewById(R.id.main_tab));
        UmengUpdateAgent.update(this);
    }

    public void onTabItemClick(View v) {
        if (mCurTabItemView == v){
            return;
        }
        if (mCurTabItemView != null){
            mCurTabItemView.setSelected(false);
        }
        mCurTabItemView = v;
        mCurTabItemView.setSelected(true);
        Fragment toFragment = null;
        String tag = null;
        switch (v.getId()){
            case R.id.main_tab:
                if (mMainFragment == null){
                    mMainFragment = new MainFragment();
                }
                tag = FRAGMENT_TAG_MAIN;
                toFragment = mMainFragment;
                break;
            case R.id.organization_tab:
                if (mOrganizationFragment == null){
                    mOrganizationFragment = new OrganizationFragment();
                }
                tag = FRAGMENT_TAG_ORGANIZATION;
                toFragment = mOrganizationFragment;
                break;
            case R.id.dynamic_tab:
                if (mDynamicFragment == null){
                    mDynamicFragment = new DynamicFragment();
                }
                tag = FRAGMENT_TAG_DYNAMIC;
                toFragment = mDynamicFragment;
                break;
            case R.id.mine_tab:
                if (mMineFragment == null){
                    mMineFragment = new MineFragment();
                }
                tag = FRAGMENT_TAG_MINE;
                toFragment = mMineFragment;
                break;
        }
        if (mFragment == toFragment){
            return;
        }
        FragmentTransaction fg = mFragmentManager.beginTransaction();
        if (toFragment.isAdded()){
            if (mFragment != null){
                fg.hide(mFragment);
            }
            fg.show(toFragment);
        }else{
            if (mFragment != null){
                fg.hide(mFragment);
            }
            fg.add(R.id.fl_page, toFragment, tag);
        }
        fg.commit();
        mFragment = toFragment;
    }
    public void next(View v){
        Intent n = new Intent(this, OrganizationDetailActivity.class);
        startActivity(n);

    }

}
