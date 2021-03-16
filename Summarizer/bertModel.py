import torch
from pytorch_pretrained_bert import BertTokenizer
from tensorflow.keras import models
from transformers import BertTokenizer, TFBertModel

from SentenceTransformer import SentenceTransformer


class BertModelx:
    def __init__(self):
        word_embedding_model = models.Transformer('bert-base-uncased')

    def transform_sentence(self, sentences, word_embedding_model=None):
        pooling_model = models.Pooling(word_embedding_model.get_word_embedding_dimension())

        model = SentenceTransformer(modules=[self.word_embedding_model, pooling_model])
        model.encode('This is an example')