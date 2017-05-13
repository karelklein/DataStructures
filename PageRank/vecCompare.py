import sys

# prints the largest entry-wise difference in two vectors, stored in files whose names 
# are given as the first two arguments to this program

f = open(sys.argv[1], 'r')

x = []
for line in f:
  x.append(float(line))


g = open(sys.argv[2], 'r')

y = []
for line in g:
  y.append(float(line))

print len(x), len(y)
  
temp = map(lambda a,b: abs(a-b), x, y)
print max(temp)

# for i in range(300):
# 	if abs(x[i]-y[i]) > 0:
# 		print i, x[i], y[i]