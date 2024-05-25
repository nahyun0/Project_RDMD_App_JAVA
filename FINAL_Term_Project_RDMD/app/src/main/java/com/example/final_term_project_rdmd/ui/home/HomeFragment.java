package com.example.final_term_project_rdmd.ui.home;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.final_term_project_rdmd.ImageAdapter;
import me.relex.circleindicator.CircleIndicator3;

import java.util.Arrays;
import java.util.List;

import com.example.final_term_project_rdmd.R;
import com.example.final_term_project_rdmd.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private int[] imageResIds = {R.drawable.img_main_rdmd_position2,
            R.drawable.img_main_rdmd_position1, R.drawable.img_main_rdmd_position3};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // ViewPager2와 CircleIndicator3 초기화 및 설정
        ViewPager2 viewPager = root.findViewById(R.id.viewPager);
        CircleIndicator3 indicator = root.findViewById(R.id.indicator);

        // ViewPager2와 어댑터 설정
        ImageAdapter adapter = new ImageAdapter(imageResIds, position -> {
            // 이미지 클릭 시 처리할 로직
            onImageClick(position);
        });
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);

        // 버튼들에 클릭 리스너 등록
        root.findViewById(R.id.mainButtonMap).setOnClickListener(this);
        root.findViewById(R.id.mainButtonCustom).setOnClickListener(this);
        root.findViewById(R.id.mainButtonCamera).setOnClickListener(this);
        root.findViewById(R.id.mainButtonRank).setOnClickListener(this);

        return root;
    }

    private void onImageClick(int position) {

        /*Toast.makeText(getContext(), "Image " + position + " clicked", Toast.LENGTH_SHORT).show();*/

        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);

        if (position == 0) {
            String webViewUrl = "https://www.hira.or.kr/ra/eval/getDiagEvlView.do?pgmid=HIRAA030004000100";
            navController.navigate(R.id.action_navigation_home_to_positionFragment);

        } else if (position == 1) {
            navController.navigate(R.id.navigation_custom);

        } else if (position == 2) {
            navController.navigate(R.id.navigation_map);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        // 클릭된 버튼에 따라 다른 fragment로 이동
        if (v.getId() == R.id.mainButtonMap) {
            navController.navigate(R.id.navigation_map);
        } else if (v.getId() == R.id.mainButtonCustom) {
            navController.navigate(R.id.navigation_custom);
        } else if (v.getId() == R.id.mainButtonCamera) {
            navController.navigate(R.id.navigation_camera);
        } else if (v.getId() == R.id.mainButtonRank) {
            navController.navigate(R.id.navigation_rank);
        }
    }
}