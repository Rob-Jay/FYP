from sentence_transformers import SentenceTransformer


class BertModel:
    def __init__(self):
        self.model = SentenceTransformer('distilbert-base-nli-stsb-mean-tokens')

    def transform_sentence(self, sentences):
        sentence_embeddings = self.model.encode(sentences)
        return sentence_embeddings
