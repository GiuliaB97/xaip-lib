import pandas as pd
import matplotlib.pyplot as plt
#from google.colab import files
import os

def filterByValue(value, greater, column, df):
  if(greater):
    df.drop(df[column > value].index, inplace = True)
  else:
    df.drop(df[column < value].index, inplace = True)

def readData(name):
  df = pd.read_csv(name, sep="\s*,\s*")
  df.drop( df[(df['PlanLength'] < 5) & (df['PlanLength'] >25)].index, inplace = True)
  return df['Domain'].iloc[0], df.drop(columns=['QuestionType'])

def aggreagteResults(df):
  to_plot = df.groupby(by='PlanLength').agg(
      TimeMean=pd.NamedAgg(column="Time", aggfunc="min"),
      TimeStdev=pd.NamedAgg(column="Time", aggfunc="std"),
      MemoryMean=pd.NamedAgg(column="Memory", aggfunc="min"),
      MemoryStdev=pd.NamedAgg(column="Memory", aggfunc="std"),
  )
  return to_plot

def plotData(to_plot, name, domain_name):
  x = to_plot.index.array
  time_y = to_plot['TimeMean'].array
  time_s = to_plot['TimeStdev'].array
  mem_y = to_plot['MemoryMean'].array
  mem_s = to_plot['MemoryStdev'].array

  fig, (ax1, ax2) = plt.subplots(1,2, figsize=(8, 2))
  fig.tight_layout(pad=1.5)
  ax1.plot(x, time_y, color='r')

  ax1.title.set_text("Time Variation")
  ax1.fill_between(x, (time_y - time_s), (time_y + time_s), color='r', alpha=.1, label ="time confidence interval")
  ax1.set(xlabel="plan length", ylabel="time mean")
  
  ax2.title.set_text("Memory Variation")
  ax2.plot(x, mem_y)
  ax2.fill_between(x,(mem_y - mem_s), (mem_y + mem_s), color='b', alpha=.1, label="memory confidence interval")
  ax2.set(xlabel="plan length", ylabel="memory mean")
  #fig.legend()
  name_figure = '{}-{}-time-memory-variation.svg'.format(domain_name, name)
  fig.savefig("{}/figures/{}".format(dir, name_figure))
  #files.download(name_figure)

def aggregateResults(time_mean, memory_mean, domain):
  l = list(zip(time_mean, memory_mean))
  aggregate = pd.DataFrame(l, index = [domain for i in range(len(l))], columns =['TimeMean', 'MemoryMean'])
  #aggregate = pd.DataFrame(list(zip(time_mean, memory_mean)), index = names, columns =['TimeMean', 'MemoryMean'])
  aggregate.to_csv('{}/aggregate_data/{}_questions_aggregate.csv'.format(dir, domain), encoding='utf-8')
  time_mean = [aggregate['TimeMean'].head(5).mean(), aggregate['TimeMean'].tail(5).mean()]
  memory_mean = [aggregate['MemoryMean'].head(5).mean(), aggregate['MemoryMean'].tail(5).mean()]
  aggregate_domain = pd.DataFrame([time_mean, memory_mean], index =['TimeMean', 'MemoryMean'], columns = ['General', 'Contrastive'])
  aggregate_domain.to_csv('{}/aggregate_data/{}_domain_aggregate.csv'.format(dir,domain), encoding='utf-8')

os_names = ["linux", "windows"]
domain_names = ["block_world", "logistic"]
explanation_names = ["contrastiveExplanation", "generalExplanation"]

names =[]

memory_mean_bw = []
time_mean_bw = []
memory_mean_l = []
time_mean_l = []
domain_name = ""
dir = '../../../res/benchmark'


for domain in domain_names:
  for explanation in explanation_names:
    for number in range(1,6):
      df_name = "{}_{}_{}_Question{}".format(domain, os_names[1],explanation, number)
      domain_name, df=readData("{}/{}.csv".format(dir, df_name))
      names.append(df_name)
      df=aggreagteResults(df)
      plotData(df, df_name, domain_name)
      print("{} =={}:{} ".format(domain, domain_names[0], (domain == domain_names[0]) ))
      if(domain == domain_names[0]):
        memory_mean_bw.append( df["MemoryMean"].mean())
        time_mean_bw.append( df["TimeMean"].mean())
      else:
        memory_mean_l.append( df["MemoryMean"].mean())
        time_mean_l.append( df["TimeMean"].mean())

aggregateResults(time_mean_bw, memory_mean_bw, domain_names[0])
aggregateResults(time_mean_l, memory_mean_l, domain_names[1])
print(dir)