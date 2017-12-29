package workshop.akbolatss.tagsnews.screen.board;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

import workshop.akbolatss.tagsnews.screen.news.NewsSource;
import workshop.akbolatss.tagsnews.util.SmartFragmentStatePagerAdapter;


public class SectionsPagerAdapter extends SmartFragmentStatePagerAdapter<Fragment> {

    private List<NewsSource> sections;

    public SectionsPagerAdapter(FragmentManager fm, List<NewsSource> sections) {
        super(fm);
        this.sections = sections;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(String.valueOf(position), sections.get(position).getName());
        bundle.putString(getPageTitle(position).toString(), sections.get(position).getUrl());
        sections.get(position).getFragment().setArguments(bundle);
        return sections.get(position).getFragment();
    }

    @Override
    public int getCount() {
        if (sections == null) {
            return 0;
        } else {
            return sections.size();
        }
    }


    public void onUpdate(List<NewsSource> newSections) {
        for (int i = getCount() - 1; i >= 1; i--) {
            sections.remove(i);
        }
        sections.addAll(newSections);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sections.get(position).getName();
    }
}
