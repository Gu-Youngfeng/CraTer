# WekaEvaluation
This project implements the model building and evaluation in paper "Does the Fault Reside in the Stack Trace". 

## Environment
This project is developed in Java 8.0. The additional plugins we used is <b>WEKA</b>, whose jar can be downloaded in the Maven Repository
<a href="http://mvnrepository.com/artifact/nz.ac.waikato.cms.weka/weka-stable">[Weka Stable]</a>.

## Package
We have two packages at present, i.e., <code>sklse.yongfeng.data</code> and <code>sklse.yongfeng.experiments</code>.

<code>sklse.yongfeng.data</code> package include,
<ul>
  <li><b>RandomGenerator.java</b> is used to randomly select/generate crashes from each project.</li>
  <li><b>InsMerge.java</b> is used to merage individual datasets into one total dataset.</li>
  <li><b>StatisticalProject.java</b> is used to provide the statistical information of each project.</li>
</ul>

<code>sklse.yongfeng.experiments</code> package include,
<ul>
  <li><b>Overall.java</b> is used to conduct 10-fold cross validation on whole dataset.</li>
  <li><b>Single.java</b> is used to conduct 10-fold cross validation on each individual dataset.</li>
  <li><b>ImbalanceProcessing.java</b> is used to conduct contrast experiment between using imbalanced data processing methods and No strategy.</li>
  <li><b>FeatureSelection.java</b> is used to conduct contrast experiment between using feature selection methods and No strategy.</li>
  <li><b>FoldResults.java</b> is used to get each fold result in cross validation, and the results will be used in the Wilcoxon signed-rank test.</li>
</ul>
