rm aa
for i in $(seq 1 100);
do
    echo $i
    java conformance.ConformanceTests >> aa
done

grep -o "passed: 23" aa | wc -l
