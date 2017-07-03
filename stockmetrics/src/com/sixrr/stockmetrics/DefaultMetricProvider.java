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

package com.sixrr.stockmetrics;

import com.sixrr.metrics.Metric;
import com.sixrr.metrics.MetricProvider;
import com.sixrr.metrics.PrebuiltMetricProfile;
import com.sixrr.stockmetrics.classMetrics.*;
import com.sixrr.stockmetrics.fileTypeMetrics.*;
import com.sixrr.stockmetrics.i18n.StockMetricsBundle;
import com.sixrr.stockmetrics.moduleMetrics.*;
import com.sixrr.stockmetrics.projectMetrics.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultMetricProvider implements MetricProvider {

    private static void initializeFileTypeMetrics(Collection<Metric> metrics) {
        metrics.add(new CommentLinesOfCodeFileTypeMetric());
        metrics.add(new CommentRatioFileTypeMetric());
        metrics.add(new LinesOfCodeFileTypeMetric());
        metrics.add(new NonCommentLinesOfCodeFileTypeMetric());
        metrics.add(new NumFilesFileTypeMetric());
        metrics.add(new TodoCommentCountFileTypeMetric());
    }

    private static void initializeModuleMetrics(Collection<Metric> metrics) {
        metrics.add(new CommentLinesOfCodeModuleMetric());
        metrics.add(new CommentRatioModuleMetric());
        metrics.add(new LinesOfCodeModuleMetric());
        metrics.add(new LinesOfHTMLModuleMetric());
        metrics.add(new LinesOfProductCodeModuleMetric());
        metrics.add(new LinesOfTestCodeModuleMetric());
        metrics.add(new LinesOfXMLModuleMetric());
        metrics.add(new NumFilesModuleMetric());
        metrics.add(new NumHTMLFilesModuleMetric());
        metrics.add(new NumXMLFilesModuleMetric());
        metrics.add(new SourceLinesOfCodeModuleMetric());
        metrics.add(new SourceLinesOfCodeProductModuleMetric());
        metrics.add(new SourceLinesOfCodeTestModuleMetric());
        metrics.add(new TestRatioModuleMetric());
        metrics.add(new TodoCommentCountModuleMetric());
        metrics.add(new TrueCommentRatioModuleMetric());
    }

    private static void initializeProjectMetrics(Collection<Metric> metrics) {
        metrics.add(new CommentLinesOfCodeProjectMetric());
        metrics.add(new CommentRatioProjectMetric());
        metrics.add(new LinesOfCodeProjectMetric());
        metrics.add(new LinesOfHTMLProjectMetric());
        metrics.add(new LinesOfProductCodeProjectMetric());
        metrics.add(new LinesOfTestCodeProjectMetric());
        metrics.add(new LinesOfXMLProjectMetric());
        metrics.add(new NumFilesProjectMetric());
        metrics.add(new NumHTMLFilesProjectMetric());
        metrics.add(new NumXMLFilesProjectMetric());
        metrics.add(new SourceLinesOfCodeProductProjectMetric());
        metrics.add(new SourceLinesOfCodeProjectMetric());
        metrics.add(new SourceLinesOfCodeTestProjectMetric());
        metrics.add(new TestRatioProjectMetric());
        metrics.add(new TodoCommentCountProjectMetric());
        metrics.add(new TrueCommentRatioProjectMetric());
    }

    private static PrebuiltMetricProfile createCodeSizeProfile() {
        final PrebuiltMetricProfile profile =
                new PrebuiltMetricProfile(StockMetricsBundle.message("lines.of.code.metrics.profile.name"));
        profile.addMetric(LinesOfCodeFileTypeMetric.class);
        profile.addMetric(LinesOfCodeModuleMetric.class);
        profile.addMetric(LinesOfCodeProjectMetric.class);
        profile.addMetric(LinesOfHTMLModuleMetric.class);
        profile.addMetric(LinesOfHTMLProjectMetric.class);
        profile.addMetric(LinesOfProductCodeModuleMetric.class);
        profile.addMetric(LinesOfProductCodeProjectMetric.class);
        profile.addMetric(LinesOfTestCodeModuleMetric.class);
        profile.addMetric(LinesOfTestCodeProjectMetric.class);
        profile.addMetric(LinesOfXMLModuleMetric.class);
        profile.addMetric(LinesOfXMLProjectMetric.class);
        profile.addMetric(NonCommentLinesOfCodeFileTypeMetric.class);
        profile.addMetric(SourceLinesOfCodeModuleMetric.class);
        profile.addMetric(SourceLinesOfCodeProductModuleMetric.class);
        profile.addMetric(SourceLinesOfCodeProductProjectMetric.class);
        profile.addMetric(SourceLinesOfCodeProjectMetric.class);
        profile.addMetric(SourceLinesOfCodeTestModuleMetric.class);
        profile.addMetric(SourceLinesOfCodeTestProjectMetric.class);
        return profile;
    }

    private static PrebuiltMetricProfile createFileCountProfile() {
        final PrebuiltMetricProfile profile =
                new PrebuiltMetricProfile(StockMetricsBundle.message("file.count.metrics.profile.name"));
        profile.addMetric(NumFilesFileTypeMetric.class);
        profile.addMetric(NumFilesModuleMetric.class);
        profile.addMetric(NumFilesProjectMetric.class);
        profile.addMetric(NumHTMLFilesModuleMetric.class);
        profile.addMetric(NumHTMLFilesProjectMetric.class);
        profile.addMetric(NumXMLFilesModuleMetric.class);
        profile.addMetric(NumXMLFilesProjectMetric.class);
        return profile;
    }

    private static PrebuiltMetricProfile createSDDRARProfile() {
        final PrebuiltMetricProfile profile =
                new PrebuiltMetricProfile("SDDRAR metrics profile 2");

//        profile.addMetric(AfferentCouplingClassMetric.class);
//        profile.addMetric(DataAbstractionCouplingClassMetric.class);
//        profile.addMetric(InformationFlowBasedCohesionClassMetric.class);
//        profile.addMetric(TightClassCouplingMetric.class);
//        profile.addMetric(LooseClassCouplingMetric.class);
//        profile.addMetric(LackOfCohesionInMethods1ClassMetric.class);
//        profile.addMetric(LackOfCohesionInMethods2ClassMetric.class);
//        profile.addMetric(LackOfCohesionInMethods5ClassMetric.class);
        profile.addMetric(LocalityOfDataClassMetric.class);
//        profile.addMetric(MessagePassingCouplingClassMetric.class);
//        profile.addMetric(NumMethodsClassMetric.class);

        //

        profile.addMetric(CouplingBetweenObjectsClassMetric.class);
        profile.addMetric(LackOfCohesionOfMethodsClassMetric.class);
        profile.addMetric(NumAttributesAddedMetric.class);
        profile.addMetric(NumAttributesInheritedMetric.class);
        profile.addMetric(NumAttributesInheritedMetric.class);
        profile.addMetric(NumCommandsClassMetric.class);
        profile.addMetric(NumOperationsAddedMetric.class);
        profile.addMetric(NumOperationsInheritedMetric.class);
        profile.addMetric(ResponseForClassMetric.class);

        ////////////////////////

        profile.addMetric(NumConstructorsMetric.class);
        profile.addMetric(AdjustedLevelOrderClassMetric.class);
        profile.addMetric(LevelOrderClassMetric.class);
        profile.addMetric(NumOperationsOverriddenMetric.class);
        profile.addMetric(AverageOperationParametersMetric.class);
        profile.addMetric(AverageOperationComplexityMetric.class);
        profile.addMetric(NumDependenciesClassMetric.class);
        profile.addMetric(NumDependentsClassMetric.class);

        //
        profile.addMetric(HalsteadBugsClassMetric.class);
        profile.addMetric(HalsteadDifficultyClassMetric.class);
        profile.addMetric(HalsteadEffortClassMetric.class);
        profile.addMetric(HalsteadLengthClassMetric.class);
        profile.addMetric(HalsteadVocabularyClassMetric.class);
        profile.addMetric(HalsteadVolumeClassMetric.class);

        //
        profile.addMetric(AverageOperationComplexityMetric.class);
        profile.addMetric(AverageOperationParametersMetric.class);

        profile.addMetric(DepthOfInheritanceMetric.class);
        profile.addMetric(AverageOperationComplexityMetric.class);
        profile.addMetric(LevelOrderClassMetric.class);
        profile.addMetric(NumAttributesInheritedMetric.class);
        profile.addMetric(ClassSizeOperationsMetric.class);
        profile.addMetric(ClassSizeOperationsAttributesMetric.class);
        profile.addMetric(ClassSizeAttributesMetric.class);
        profile.addMetric(HalsteadEffortClassMetric.class);
        profile.addMetric(AverageOperationSizeMetric.class);
        profile.addMetric(AdjustedLevelOrderClassMetric.class);
        profile.addMetric(NumOperationsOverriddenMetric.class);
        profile.addMetric(MaximumOperationSizeMetric.class);
        profile.addMetric(MaximumOperationComplexityMetric.class);
        profile.addMetric(NumInterfacesImplementedMetric.class);
        profile.addMetric(CommentLinesOfCodeClassMetric.class);
        profile.addMetric(CouplingBetweenObjectsClassMetric.class);
        profile.addMetric(NumAttributesAddedMetric.class);

        return profile;
    }

    @NotNull
    @Override
    public List<Metric> getMetrics() {
        final List<Metric> metrics = new ArrayList<Metric>(38);
        initializeFileTypeMetrics(metrics);
        initializeModuleMetrics(metrics);
        initializeProjectMetrics(metrics);
        return metrics;
    }

    @NotNull
    @Override
    public List<PrebuiltMetricProfile> getPrebuiltProfiles() {
        final List<PrebuiltMetricProfile> out = new ArrayList<PrebuiltMetricProfile>(2);
        out.add(createCodeSizeProfile());
        out.add(createFileCountProfile());
        out.add(createSDDRARProfile());
        return out;
    }
}
