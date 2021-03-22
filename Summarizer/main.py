from sentence_handler import *
from bertModel import *
from cluster_handler import *
import time
import numpy as np
import sys
import torch


k = int(sys.argv[1])
type(k)


text = (sys.argv[2])
type(k)
start_time = time.time()


model = BertModel()
cluster_model = Clusterer()
sentence_handler = SentenceHandler()
sentences = sentence_handler.tokenize(text)

if k > len(sentences):
    k = len(sentences)


sentence_embeddings = model.get_embeddings(sentences)
values = cluster_model.cluster(sentence_embeddings, k)
sorted_values = sorted(values)
for i, sentence in enumerate(sorted_values):
    print("Sentence ", i + 1, " ", sentences[sentence], "\n")

print("My program took", time.time() - start_time, "to run")
print(100-(k/len(sentences)*100), "% has been reduced")