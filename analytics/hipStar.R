#
# DeployR Data IO Example R Script
#
# Based on the Hipparcos Star Dataset:
# http://astrostatistics.psu.edu/datasets/HIP_star.html
#
if(exists('hipStarUrl')) {
  hip = read.table(hipStarUrl, header=T,fill=T)
  print('Hip Star data.frame read from URL.')
} else
if(file.exists('hipStar.dat')) {
  hip = read.table('hipStar.dat', header=T,fill=T)
  print('Hip Star data.frame read from file, hipStar.dat.')
} else {
  # Else assume "hip" preloaded from DeployR-repository
  # binary R object file into workspace.
  print('Hip Star data.frame preloaded from hipStar.rData.')
}
  
# Example R Script outputs.
hipDim = dim(hip)
hipNames = names(hip)
hipSubset = hip[1:10,]