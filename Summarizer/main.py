from sentence_handler import *
from bert_model import *
from cluster_handler import *
import time

i = 0
for i in range(10):
    start_time = time.time()

    text = """"An ongoing outbreak of pneumonia associated with a novel coronavirus, severe acute respiratory syndrome (SARS) coronavirus 2, was reported in Wuhan, Hubei Province, China, in December 2019 (1–3). In the following weeks, infections spread across China and other countries around the world (4–6). The Chinese public health, clinical, and scientific communities took action to allow for timely recognition of the new virus and shared the viral gene sequence to the world (2,7). On January 30, 2020, the World Health Organization (WHO) declared the outbreak a Public Health Emergency of International Concern (8). On February 12, 2020, the WHO named the disease caused by the novel coronavirus “coronavirus disease 2019” (COVID-19) (9). A group of international experts, with a range of specializations, have worked with Chinese counterparts to try to contain the outbreak (10).
     
     At present, a real-time reverse-transcription polymerase chain reaction (RT-PCR) assay for COVID-19 has been developed and used in clinics. Although RT-PCR remains the reference standard for making a definitive diagnosis of COVID-19 infection (11), the high false-negative rate (12) and the unavailability of the RT-PCR assay in the early stage of the outbreak restricted prompt diagnosis of infected patients. Radiologic examinations, especially thin-slice chest CT, play an important role in fighting this infectious disease (13). Chest CT can help identify the early phase lung infection (14,15) and prompt larger public health surveillance and response systems (16). Currently, chest CT findings have been recommended as major evidence for confirmed clinical diagnosis in Hubei, China. The addition of chest CT for diagnosis resulted in 14 840 confirmed new cases (13 332 clinically diagnosed cases) reported on February 13, 2020. Comprehensive and timely review of the role of radiology in fighting COVID-19 remains urgent and mandatory.
     
     Etiology
     In a preliminary report, complete viral genome analysis revealed that the virus shared 88% sequence identity to two bat-derived SARS-like coronaviruses, but more distant from SARS coronavirus (17). Hence, the virus was temporarily called 2019 novel coronavirus (2019-nCoV). Coronavirus is an enveloped and single-stranded ribonucleic acid named for its solar corona-like appearance due to 9–12-nm-long surface spikes (18). There are four major structural proteins encoded by the coronaviral genome on the envelope, one of which is the spike protein (S) that binds to angiotensin-converting enzyme 2 receptor and mediates subsequent fusion between the envelope and host cell membranes to aid viral entry into the host cell (19,20). On February 11, 2020, the Coronavirus Study Group of the International Committee on Taxonomy of Viruses finally designated it as SARS coronavirus 2 based on phylogeny, taxonomy, and established practice (21). Shortly thereafter, the WHO named the disease caused by this coronavirus COVID-19 (9). On the basis of current data, it seems that SARS coronavirus 2 might be initially hosted by bats and might have been transmitted to humans by means of pangolin (22) or other wild animals (17,23) sold at the Huanan Seafood Market but subsequently spread by means of human-to-human transmission.
     
     Epidemiology
     In December 2019, the earliest symptoms of patients confirmed to have COVID-19 appeared (24). At first, the morbidity remained low. However, it reached a tipping point in the middle of January 2020. During the second half of that month, there was a remarkable increase in the number of infected patients in affected cities outside Hubei Province because of the population movement before the lunar Chinese New Year (25). Followed by an exponential growth until January 23, 2020, the outbreak spread to the other countries, attracting extensive attention around the world (Fig 1). Evidence of clusters of infected family members and medical workers confirmed the presence of human-to-human transmission (12) by droplets, contact, and fomite (26,27). Thus far, there is no definite evidence of intrauterine transmission (28). Current estimates are that COVID-19 has a median incubation period of 3 days (range, 0–24 days), with potential transmission from asymptomatic individuals (26,29). At the end of January 2020, the WHO confirmed that there were more than 10 000 cases of COVID-19 across China (30). On February 13, 2020, 13 332 new clinically diagnosed cases were first reported from Hubei. Official reports included clinically diagnosed cases and laboratory-confirmed cases because chest CT findings were recommended as the major evidence for clinically confirmed cases in the Diagnosis and Treatment Program of 2019 New Coronavirus Pneumonia (trial version 5) by the National Health and Health Commission of China in February 2020 (13). Fishing is fun we should fish.  As of February 19, 2020, the total number of confirmed cases rose to 74 280 in China and to 924 in 25 countries outside China; there was a total of 2009 deaths globally (10) (Fig 2). To control COVID-19, effective prevention and control measurements must include early detection, diagnosis, treatment, and quarantine to block human-to-human transmission and reduce secondary infections among close contacts and health care workers (10).
     """

    sentence_handler = SentenceHandler()
    sentences = sentence_handler.tokenize(text)

    model = BertModel()
    sentence_embeddings = model.transform_sentence(sentences)

    cluster_model = Clusterer()
    values = cluster_model.cluster(sentence_embeddings, 5)
    sorted_values = sorted(values)
    # for i, sentence in enumerate(sorted_values):
    #     print("Sentence ", i + 1, " ", sentences[sentence], "\n")

    print("My program took", time.time() - start_time, "to run")
