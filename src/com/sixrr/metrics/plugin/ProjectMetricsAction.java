/*
 * Copyright 2005-2016 Sixth and Red River Software, Bas Leijdekkers
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.sixrr.metrics.plugin;

import com.intellij.analysis.AnalysisScope;
import com.intellij.analysis.BaseAnalysisAction;
import com.intellij.analysis.BaseAnalysisActionDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.wm.ToolWindowManager;
import com.simiyutin.au.sddrar.SDDRARioHandler;
import com.sixrr.metrics.Metric;
import com.sixrr.metrics.MetricCategory;
import com.sixrr.metrics.config.MetricsReloadedConfig;
import com.sixrr.metrics.metricModel.MetricsExecutionContextImpl;
import com.sixrr.metrics.metricModel.MetricsResult;
import com.sixrr.metrics.metricModel.MetricsRunImpl;
import com.sixrr.metrics.metricModel.TimeStamp;
import com.sixrr.metrics.profile.MetricsProfile;
import com.sixrr.metrics.profile.MetricsProfileRepository;
import com.sixrr.metrics.ui.dialogs.ProfileSelectionPanel;
import com.sixrr.metrics.ui.metricdisplay.MetricsToolWindow;
import com.sixrr.metrics.utils.MetricsReloadedBundle;
import org.apache.commons.math3.linear.MatrixUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.simiyutin.au.sddrar.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectMetricsAction extends BaseAnalysisAction {

    public ProjectMetricsAction() {
        super(MetricsReloadedBundle.message("metrics.calculation"), MetricsReloadedBundle.message("metrics"));
    }

    @Override
    protected void analyze(@NotNull final Project project, @NotNull final AnalysisScope analysisScope) {
        final MetricsProfileRepository repository = MetricsProfileRepository.getInstance();
        final MetricsProfile profile = repository.getCurrentProfile();
        final MetricsToolWindow toolWindow = MetricsToolWindow.getInstance(project);
        final MetricsRunImpl metricsRun = new MetricsRunImpl();
        new MetricsExecutionContextImpl(project, analysisScope) {

            @Override
            public void onFinish() {
                final boolean showOnlyWarnings = MetricsReloadedConfig.getInstance().isShowOnlyWarnings();
                if(!metricsRun.hasWarnings(profile) && showOnlyWarnings) {
                    ToolWindowManager.getInstance(project).notifyByBalloon(MetricsToolWindow.METRICS_TOOL_WINDOW_ID,
                            MessageType.INFO, MetricsReloadedBundle.message("no.metrics.warnings.found"));
                    return;
                }
                final String profileName = profile.getName();
                metricsRun.setProfileName(profileName);
                metricsRun.setContext(analysisScope);
                metricsRun.setTimestamp(new TimeStamp());
                dumpForSDDRAR(metricsRun);
                toolWindow.show(metricsRun, profile, analysisScope, showOnlyWarnings);
            }
        }.execute(profile, metricsRun);
    }

    private void dumpForSDDRAR(MetricsRunImpl metricsRun) {
        DataSet dataSet = extractDataSet(metricsRun);
        SDDRARioHandler.dump(dataSet, metricsRun.getProfileName());
    }

    private DataSet extractDataSet(MetricsRunImpl metricsRun) {

        MetricsResult result = metricsRun.getResultsForCategory(MetricCategory.Class);
        List<String> entityNames = Arrays.asList(result.getMeasuredObjects());
        List<String> featureNames = new ArrayList<>();
        for (Metric metric : result.getMetrics()) {
            featureNames.add(metric.getAbbreviation());
        }

        Metric[] metrics = result.getMetrics();

        double[][] data = new double[entityNames.size()][featureNames.size()];
        for (int i = 0; i < entityNames.size(); i++) {
            for (int j = 0; j < featureNames.size(); j++) {
                Metric metric = metrics[j];
                String entity = entityNames.get(i);
                Double value = result.getValueForMetric(metric, entity);
                data[i][j] = value == null ? 0 : value;
            }
        }

        DataSet dataSet = new DataSet(MatrixUtils.createRealMatrix(data), entityNames, featureNames);
        return dataSet;
    }

    @Override
    @Nullable
    protected JComponent getAdditionalActionSettings(Project project, BaseAnalysisActionDialog dialog) {
        return new ProfileSelectionPanel(project);
    }
}
