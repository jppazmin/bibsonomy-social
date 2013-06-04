#!/bin/bash
echo
echo
echo "CRF training with Mallet"
echo
echo "requirements (all in the same directory like this script):"
echo "* mallet.jar"
echo "* emtpy folder crfBibsonomy"
echo "* folder testdata (contains training/testing data as txt files)"
echo "* tagged_ie_references_newreference_start_newline_meta_reference_count_cluster_count.txt in folder testdata"
echo
echo "Default Settings:"
echo "* training/testing data: testdata/tagged_ie_references_newreference_start_newline_meta_reference_count_cluster_count.txt"
echo "* segementation of the training/testing data: training 70%, testing 30%"
echo "* output folder: crfBibsonomy"
echo "* training repetition: 100"
echo "* standard out is logged in training_log.txt"
echo "* standard err is logged in training_err_log.txt"
echo
echo "With default settings the training takes ~20h, because of the 100 repetitions."
echo
echo "The best trained CRF is stored in crfBibsonomy with the name crf-saved.dat"
echo
echo "For using the trained CRF in BibSonomy copy the crf-saved.dat to bibsonomy-scraper/src/main/resources/org/bibsonomy/scraper/ie and rename it to crf.dat"
echo
echo "If a bad version error oucccurs during starting the training, the used java ist not version 5. Change path to the correct java version."
echo


#
# location of local Maven repository
#
MV=/home/rja/.m2/repository

# 
# paths of JARs of libraries relative to Maven repository 
# 
SCRAPER=org/bibsonomy/bibsonomy-scraper/2.0.1/bibsonomy-scraper-2.0.1.jar
MALLET=edu/umass/cs/mallet/mallet/0.4-jaeschke/mallet-0.4-jaeschke.jar
BSH=bsh/bsh/1.3.0/bsh-1.3.0.jar

JARS=$MV/$SCRAPER:$MV/$MALLET:$MV/$BSH


MAIN=edu.umass.cs.mallet.util.bibsonomy.TUI_IE_CRF

JAVA=$JAVA_HOME/bin/java

$JAVA -Xmx1400m \
    -cp $JARS \
    $MAIN \
    --head-or-ref 1 \
    --input-file testdata/tagged_ie_references_newreference_start_newline_meta_reference_count_cluster_count.txt \
    --use-cluster-features false \
    --training-pct 0.7 \
    --testing-pct 0.3 \
    --output-prefix crfBibsonomy \
    --num-reps 100 \
    1> training_log.txt 2> training_err_log.txt