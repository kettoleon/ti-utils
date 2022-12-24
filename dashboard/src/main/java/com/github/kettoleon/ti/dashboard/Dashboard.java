package com.github.kettoleon.ti.dashboard;

import com.github.kettoleon.ti.dashboard.model.DashboardModel;
import com.github.kettoleon.ti.dashboard.view.DashboardView;
import com.github.kettoleon.ti.dashboard.view.SimpleSwingDashboardView;
import com.github.kettoleon.ti.meta.MetaRepository;
import com.github.kettoleon.ti.saves.auto.AutoSaveFileWatcher;

import java.nio.file.Path;

public class Dashboard {

    public static void main(String[] args) {
        //TODO parameterize saves folder
        MetaRepository metaRepository = new MetaRepository(Path.of("E:\\SteamLibrary\\steamapps\\common\\Terra Invicta\\TerraInvicta_Data\\StreamingAssets\\Templates"));
        DashboardModel dashboardModel = new DashboardModel();
        DashboardView dashboardView = new SimpleSwingDashboardView(dashboardModel, metaRepository); //Will look for changes in the model?
        DashboardController dashboardController = new DashboardController(dashboardModel, metaRepository); //Changes in the model will trigger ui update

        dashboardView.start();
        new AutoSaveFileWatcher(Path.of("C:\\Users\\enrodri86\\Documents\\My Games\\TerraInvicta\\Saves"), dashboardController::onSaveFileUpdated).run();
    }

}
