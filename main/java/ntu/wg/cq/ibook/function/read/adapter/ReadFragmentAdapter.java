package ntu.wg.cq.ibook.function.read.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import ntu.wg.cq.ibook.function.read.view.fragment.ReadFragment;

/**
 * Created by C_Q on 2018/2/9.
 */

public class ReadFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;

    public ReadFragmentAdapter(FragmentManager fragmentManager,List<Fragment> list){
        super(fragmentManager);
        this.list=list;
    }
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void addPage(ReadFragment readFragment){
        list.add(readFragment);
        notifyDataSetChanged();
    }

    public void deletePage(){

    }
}
