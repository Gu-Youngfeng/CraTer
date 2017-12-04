# CraTer

[CraTer](https://github.com/Gu-Youngfeng/CraTer) is an open-source Java tool which implements the **evaluation process** in paper "Does the Fault Reside in Stack Trace". It takes the datasets (arff format) extracted from other Java projects as input, and outputs the evalution results of the prediction approach proposed in the paper. 

### Environment

This project is developed in Java 8.0. The additional plugins we used is **WEKA**, whose jar can be downloaded in the Maven Repository [Weka Stable](http://mvnrepository.com/artifact/nz.ac.waikato.cms.weka/weka-stable).

### Construction

Currently, we have only 3 packages at present, i.e., `sklse.yongfeng.data`, which does some data preprocessing work, `sklse.yongfeng.experiments`, which contains the whole experiments processes, and `sklse.yongfeng.launcher`, which provide the main entry of the project.

1.`sklse.yongfeng.data` package includes,

- **FilesSearcher.java** provide different file searchers for searching suitable datasets.
- **RandomGenerator.java** is used to randomly select/generate crashes from each project.
- **InsMerge.java** is used to merage individual datasets into one total dataset.
- **StatisticalProject.java** calculates the statistical information(distribution of **inTrace** and **outTrace** instances) of each dataset.

2.`sklse.yongfeng.experiments` package includes,

- **Overall.java** evaluates the prediction approach on the total datasets combined with 7 projects using cross validation.
- **Single.java** evaluates the prediction approach on each project using cross validation.
- **ImbalanceProcessingAve.java** conducts the contrast experiment between using imbalanced data processing methods and No strategy.
- **FeatureSelectionAve.java** conducts the contrast experiment between using feature selection methods and No strategy.
- **FeatureRankingAve.java** ranks the features by Pearson's Correlation, then output the top-10 feature list of each project.
- **TopTenFeatureEvaluation.java** evaluate the prediction approach on the each project using only top-10 features.
- **FoldResultsAve.java** is used to get each fold result in cross validation, and the results will be used in the Wilcoxon signed-rank test.

3. `sklse.yongfeng.launcher` package includes,

- **Launcher.java** provides the main entry of the project.

4. `files` folder includes,

- all dataset used in our experiments.

5. `libs` folder includes,

- all dependency jar files used in our project.

6. `CraTer.jar`

- `CraTer.jar` is the core part of the project, we can run it directly on terminate or CMD windows.

### Usage

There are two main way to run the **CraTer** on your computer, and it's also a easy work to conduct secondary development on **CraTer**.

- Import the **CraTer project** into Eclipse, there is almost main function in every java files. Click `run as Java application` to get the experimental results.

- Use **CraTer.jar** contained in the project. try to use it on terminate or CMD windows, the commands are quite simple, such as `java -jar CraTer.jar -help` to show the description and help information of the **CraTer** project.
