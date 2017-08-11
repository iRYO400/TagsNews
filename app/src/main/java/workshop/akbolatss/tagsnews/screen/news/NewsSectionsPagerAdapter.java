package workshop.akbolatss.tagsnews.screen.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by AkbolatSS on 09.08.2017.
 */

public class NewsSectionsPagerAdapter extends FragmentStatePagerAdapter {

    List<NewsSource> sections;

    public NewsSectionsPagerAdapter(FragmentManager fm, List<NewsSource> sections) {
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
        if (sections == null){
            return 0;
        } else {
            return sections.size();
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return sections.get(position).getName();
    }
}
