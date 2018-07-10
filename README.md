# CraTer

[CraTer](https://github.com/Gu-Youngfeng/CraTer) is an open-source Java tool which is the prototype in paper _Does the Fault Reside in a Stack Trace?_. It first extracts features from the crash datasets, then uses these features to train a predictive model. Once a new crash is submitted, we can use this built model to predict the fault location(i.e., InTrace or OutTrace) of the crash.  

## 1. Environment

This project is developed in Java 8.0. The external library we used is **WEKA**, whose jar can be downloaded in the Maven repository [Weka Stable](http://mvnrepository.com/artifact/nz.ac.waikato.cms.weka/weka-stable). Another library is **SPOON**, whose jar can be downloaded in the Maven repository [Spoon Core](http://mvnrepository.com/artifact/fr.inria.gforge.spoon/spoon-core).

## 2. Construction

Currently, we have only four packages at present, i.e., `sklse.yongfeng.analysis`, which conducts static analysis on method, class, and project, `sklse.yongfeng.data`, which does some data preprocessing work, `sklse.yongfeng.experiments`, which contains the whole experiments processes, and `sklse.yongfeng.launcher`, which provide the main entry of the project.

`sklse.yongfeng.analysis` packages includes,

- **RepsUtilier.java** splits the single crash from stack traces file.
- **CrashNode** saves detailed information of each crash.
- **MEDAnalyzer.java** analyzes the features of given method. 
- **CLSAnalyzer.java** analyzes the features of given class.
- **SRCAnalyzer.java** analyzes the features of given project.

`sklse.yongfeng.data` package includes,

- **FilesSearcher.java** provide different file searchers for searching suitable datasets.
- **RandomGenerator.java** is used to randomly select/generate crashes from each project.
- **InsMerge.java** is used to merage individual datasets into one total dataset.
- **StatisticalProject.java** calculates the statistical information(distribution of **inTrace** and **outTrace** instances) of each dataset.

`sklse.yongfeng.experiments` package includes,

- **Overall.java** evaluates the prediction approach on the total datasets combined with 7 projects using cross validation.
- **Single.java** evaluates the prediction approach on each project using cross validation.
- **ImbalanceProcessingAve.java** conducts the contrast experiment between using imbalanced data processing methods and No strategy.
- **FeatureSelectionAve.java** conducts the contrast experiment between using feature selection methods and No strategy.
- **FeatureRankingAve.java** ranks the features by Pearson's Correlation, then output the top-10 feature list of each project.
- **TopTenFeatureEvaluation.java** evaluate the prediction approach on the each project using only top-10 features.
- **FoldResultsAve.java** is used to get each fold result in cross validation, and the results will be used in the Wilcoxon signed-rank test.

`sklse.yongfeng.launcher` package includes,

- **Launcher.java** provides the main entry of the project.

Beside the packages presented above, the `CraTer.jar` is the core part of the project, we can run it directly on terminate or CMD windows. What'more, `libs` folder includes all dependency jar files used in our project.`files` folder includes all dataset used in our experiments. `resources/crashrep` collects stack trace files of each project, `resources/projs` saves all source codes of each project, `resources/projs_libs` preserves jar formats of each project.

## 3. Usage

There are two main way to run the **CraTer** on your computer, and it's also a easy work to conduct secondary development on **CraTer**.

**[Method 1]**: Import the **CraTer project** into Eclipse, there is almost a main function in every java file. Click `run as Java application` to get the experimental results. For example, if we set the arguments of run configuration to `-help`, we will see,

![carter-eclipse](img/crater2.jpg)

**[Method 2]**: Use the **CraTer.jar** contained in the project. try to use it on terminate or CMD windows[^1]. The commands are quite simple, for example, try to type `java -jar CraTer.jar -help` on CMD windows to show the description and help information of the **CraTer** project.

![carter-cmd](img/crater.jpg)

[^1]: when you are running runable jar (CraTer.jar) on CMD windows, make sure that the jar file is in the root directory of project. 


