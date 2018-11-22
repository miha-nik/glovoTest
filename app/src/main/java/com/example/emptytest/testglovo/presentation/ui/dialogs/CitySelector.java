package com.example.emptytest.testglovo.presentation.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.example.emptytest.testglovo.R;
import com.example.emptytest.testglovo.data.models.CitiesList;
import com.example.emptytest.testglovo.data.models.City;
import com.example.emptytest.testglovo.data.models.Item;
import com.example.emptytest.testglovo.data.models.Country;
import com.example.emptytest.testglovo.data.models.GlovoData;
import com.example.emptytest.testglovo.data.repositories.Repository;
import com.example.emptytest.testglovo.domain.DataReceiver;
import com.example.emptytest.testglovo.presentation.ui.adapters.DataAdapter;

import java.util.ArrayList;
import java.util.List;

public class CitySelector extends DialogFragment implements DataAdapter.ClickListener {

    public static final String TAG = "dialogs.CitySelector";
    private View mView;
    CitiesList mDataList;
    private RecyclerView mCountryList;
    private DataAdapter mAdapter;


    private OnCitySelectorInteractionListener mListener;

    public CitySelector() {
        // Required empty public constructor
    }

    public static CitySelector newInstance() {
        return new CitySelector();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_city_selector, container, false);

        mCountryList = mView.findViewById(R.id.citiesList);
        mCountryList.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        mCountryList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DataAdapter(getActivity(),this);
        mCountryList.setAdapter(mAdapter);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Repository repository = new Repository();
        repository.getDataFromNetwork(new DataReceiver<GlovoData>() {
            @Override
            public void onSuccess(GlovoData data) {

                mDataList = new CitiesList();
                List<Item> listOfElements = new ArrayList<>();

                for(Country country: data.countries){
                    Item c = new Item();
                    c.setName(country.getName());

                    listOfElements.add(c);

                    for(City city: data.cities){
                        if(city.getCountryCode().equals(country.getCode())){
                            Item s = new Item();
                            s.setName(city.getName());
                            s.setCity(city);
                            listOfElements.add(s);
                        }
                    }
                }
                mDataList.setListOfElements(listOfElements);
                mAdapter.setData(mDataList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnCitySelectorInteractionListener) {
//            mListener = (OnCitySelectorInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    public void setListener(OnCitySelectorInteractionListener listener){
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(City city) {
        if(mListener!=null){
            mListener.onCitySelected(city);
        }
        dismiss();
    }

    public interface OnCitySelectorInteractionListener {
        void onCitySelected(City city);
    }
    @Override
    public void onResume() {
        super.onResume();
        final Window window=getDialog().getWindow();
        if(window!=null)
        {
            final ConstraintLayout ascAdContaner = (ConstraintLayout) mView.findViewById(R.id.selectDialog);
            ViewTreeObserver vto = ascAdContaner.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Display display=getActivity().getWindowManager().getDefaultDisplay();
                    DisplayMetrics metricsB=new DisplayMetrics();
                    display.getMetrics(metricsB);
                    window.setLayout((int)(metricsB.widthPixels*0.9f), (int)(metricsB.heightPixels*0.9f));//height+h1+h2+(int)(height*0.3));//
                    window.setGravity(Gravity.CENTER);
                    mView.invalidate();
                }
            });
        }
    }
}
