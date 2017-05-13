import random
import time
import matplotlib.pyplot as plt
import matplotlib.ticker as mtick
import numpy as np

############# Bloom Filter ##############

# initialize table H and seeds for hash_1
def init():
	global H, seeds
	H = [0] * n
	seeds = []
	for i in range(k):
		seeds.append(time.clock())

# initialize table H and a,b for hash_2
def init2():
	global H, a, b
	H = [0] * n
	a, b = [], []
	for i in range(k):
		a.append(random.randint(1,n-1))
		b.append(random.randint(0,n-1))

#hash function 1
def hash_1(x,ki):
	random.seed(seeds[ki] + x)
	return random.randint(0,n-1)

#hash function 2
def hash_2(x,ki):
	return (a[ki] * x + b[ki]) % n

def add(x):
	for i in range(k):
		H[hash_1(x,i)] = 1

def contains(x):
	for i in range(k):
		if H[hash_1(x,i)] != 1:
			return 'F'
	return 'T'

def main():
	global N, n, k

	# get inputs
	N = int(raw_input('N: '))
	n = int(raw_input('n: '))
	m = int(raw_input('m: '))
	a = []
	for i in range(m):
		a.append(int(raw_input('a_' + str(i) + ': ')))
	t = int(raw_input('t: '))
	b = []
	for i in range(t):
		b.append(int(raw_input('b_' + str(i) + ': ')))

	k = 10
	init()
	# outputs
	for i in a:
		add(i)
	for j in b:
		print contains(j)

######## Experiments and Plotting #########

def plot_hash():
	N = 1e6
	num = 1000
	global n
	n = 101
	x = np.random.randint(0,N,num)
	y1 = [hash_1(i) for i in x]
	y2 = [hash_2(j) for j in x]
	y1_load = [0] * n
	y2_load = [0] * n

	fig = plt.figure()
	ax1 = fig.add_subplot(121)
	ax2 = fig.add_subplot(122)
	ax1.grid(True)
	ax2.grid(True)
	ax1.set_title('hash_1 Scatter Plot')
	ax2.set_title('hash_2 Scatter Plot')
	ax1.set_xlabel('x')
	ax2.set_xlabel('x')
	ax1.set_ylabel('hash_1(x)')
	ax2.set_ylabel('hash_2(x)')
	ax1.xaxis.set_major_formatter(mtick.FormatStrFormatter('%.1e'))
	ax2.xaxis.set_major_formatter(mtick.FormatStrFormatter('%.1e'))
	
	ax1.scatter(x,y1,color='blue')
	ax2.scatter(x,y2,color='red')

	plt.show()

def get_loads(num_runs):
	N = 1e6
	num = 1000
	global n
	n = 1009
	y1_maxload,y1_minload,y2_maxload,y2_minload = [],[],[],[]
	for run in range(num_runs):
		x = np.random.randint(0,N,num)
		y1 = [hash_1(i) for i in x]
		y2 = [hash_2(j) for j in x]
		y1_load = [0] * n
		y2_load = [0] * n
		
		# get min and max load for each hash function
		for y_1 in y1:
			y1_load[y_1] += 1

		for y_2 in y2:
			y2_load[y_2] += 1
		
		y1_maxload.append(np.max(y1_load))
		y1_minload.append(min(i for i in y1_load if i > 0))
		y2_maxload.append(np.max(y2_load))
		y2_minload.append(min(i for i in y2_load if i > 0))

	y1_avgmax = np.average(y1_maxload)
	y2_avgmax = np.average(y2_maxload)
	y1_avgmin = np.average(y1_minload)
	y2_avgmin = np.average(y2_minload)

	y1_avgs = (y1_avgmin, y1_avgmax)
	y2_avgs = (y2_avgmin, y2_avgmax)

	return y1_avgs, y2_avgs

def plot_fp():
	global N, n, k
	N = 1e6
	m = 500
	K = 50

	cRange = [1,2,4,8,16]
	fpr_all = []
	for c in cRange:
		n = c * m
	
		fpr = []
		for k_ in range(1,K):
			k = k_ # assign k_ to global k
			init2()
			fp_count = 0
			for x in range(m):
				add(x)
			for y in range(m,2*m):
				if contains(y) == 'T':
					fp_count += 1
			print 'c=%s,n=%s,k=%s,fpr=%s'%(c,n,k_,float(fp_count)/m)
			fpr.append(float(fp_count)/m)

		fpr_all.append(fpr) # one fpr array per c value

	fig = plt.figure()
	ax = fig.add_subplot(111)
	plt.plot(range(1,K), fpr_all[0], label='c = 1')
	plt.plot(range(1,K), fpr_all[1], label='c = 2')
	plt.plot(range(1,K), fpr_all[2], label='c = 4')
	plt.plot(range(1,K), fpr_all[3], label='c = 8')
	plt.plot(range(1,K), fpr_all[4], label='c = 16')

	ax.set_ylim([-0.02,1.02])
	ax.set_title('False Positive Rate for various C')
	ax.set_xlabel('k')
	ax.set_ylabel('False Positive Rate')
	ax.legend(loc='center right')
	plt.show()

############################################

if __name__ == '__main__':
	main()