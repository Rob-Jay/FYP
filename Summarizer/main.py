from sentence_handler import *
from bert_model import *
from cluster_handler import *
import time
import sys

k = int(sys.argv[1])
type(k)
start_time = time.time()

text = "The Chrysler Building, the famous art deco New York skyscraper, will be sold for a small fraction of its " \
       "previous sales price "



sentence_handler = SentenceHandler()
sentences = sentence_handler.tokenize(text)

if k > len(sentences):
    k = len(sentences)

model = BertModel()
sentence_embeddings = model.transform_sentence(sentences)

cluster_model = Clusterer()
values = cluster_model.cluster(sentence_embeddings, k)
sorted_values = sorted(values)
for i, sentence in enumerate(sorted_values):
    print("Sentence ", i + 1, " ", sentences[sentence], "\n")

print("My program took", time.time() - start_time, "to run")
print(100-(k/len(sentences)*100), "% has been reduced")