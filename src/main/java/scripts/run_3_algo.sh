for i in $(seq 0 2)
do
	echo $i
	for j in $(seq 0 100)
		do
			echo $j
			java Main $i 1 $j 1 16
		done
done
