
# coding: utf-8

# In[1]:


# ### STEVE ###

# I've emailed you two files, the first is the zip file of the directory, the second is this, I now realize
# how pointless it is to leave this note in this file.

# STEP 1: Place this file in whatever directory you want. The backup files will be placed in this file's
#         diretory at 'backup/'.
        
#         IF YOU RUN THE CODE TWICE, YOU WILL OVERWRITE EVERYTHING IN backup/ SO BE CAREFUL, YOU SHOULD ONLY NEED
#         TO RUN THIS ONCE.
        
# STEP 2:
#     Run the following in the Anaconda prompt (run them one line at a time):
#         conda install -c anaconda numpy
#         conda install -c anaconda pillow
        
# STEP 3: Hit shift-enter on the cell right below this one, if you get an error, that means not all the packages were
#         installed correctly. If they aren't refer to the help section below. If that cell runs fine, hit shift-enter
#         multiple times to get to the last cell, and run it.
        
#         The data is expected to be in '../Text_Reader_Data/TrainSet/', Modify this path depending on where you place
#         the data. Make sure that you are going to /TrainSet/, that is all that matters.
        
# STEP 4: Once you see: "--- COMPLETED READING IN DATA ---", that means all the data has been read in. Check the backup/
#         directory to make sure there are 3 files in there, DICT, DATA, LABELS. If those aren't in there we don't
#         have the base data to look back at.
        
# STEP 5: Let it run. 
#             - If you want to wait and see if a single model finishes, you can wait for a print statement to show up
#               saying the model is completed, and then two more files should appear in the backup/ directory
            
# HELP SECTION:
#     If you experience any un-expected difficulties, turbulence, or otherwise unknown errors, please contact in the
#     following order:
#         Wonder Boy: (408)857-1646
#         Bob Marley: (Deceased)
#         Our Lord and Savior Jesus Christ: (Place hands together and pray, in case of spotty reception, use temple)
#         Richard Hansen: (408)857-1646


# In[2]:


import numpy as np
from PIL import Image
import io
import os
import math
import sys


# In[15]:


def img_to_binary(img, thresh=150):
    #Converts PIL image to binary np array
    if type(img) != np.ndarray:
        img = img.resize((28,28), Image.ANTIALIAS)
        x = np.array(img)
    else:
        x = img
    #print(x.shape)
    if len(x.shape) > 2:
        x = x[:,:,0]

    x[x < thresh] = 0
    x[x != 0] = 1
    
    if x[0][0] == 1:
        x[x == 0] = 2
        x[x == 1] = 0
        x[x == 2] = 1
        
    return x


# In[133]:


def feature_vector(img, window_size=2):
    #create a feature vector from a binary array
    averages_list = []
    r,c = img.shape
    for i in range(r-window_size):
        for j in range(c-window_size):
            x = img[i:i+window_size,j:j+window_size]
            averages_list.append(np.mean(x))
            
    diago = []
    diago_inv = []
    vert = []
    horiz = []
    for i in range(r):
        for j in range(c):
            if i == j:
                diago.append(img[i][j])
                
            if i == c - j - 1:
                diago_inv.append(img[i][j])
                
            if i == r/2:
                horiz.append(img[i][j])
                
            if j == c/2:
                vert.append(img[i][j])
                
    averages_list.append(val_switch(diago))
    averages_list.append(val_switch(diago_inv))
    averages_list.append(val_switch(vert))         
    averages_list.append(val_switch(horiz))
            
    return np.array(averages_list)


# In[131]:


def val_switch(arr):
    switch = arr[0]
    count = 0
    for val in arr:
        if val != switch:
            count += 1
            switch = val
            
    if count % 2 == 1:
        count += 1
        
    return count/2


# In[116]:


# Input: numpy vector x of d rows, 1 column
# numpy vector z of d rows, 1 column
# Output: kernel K(x,z) = exp(-1/2 * norm(x-z)^2)
# Example on how to call the script:
# import K
# v = K.run( np.array([[1], [4], [3]]) , np.array([[2], [7], [-1]]) )
def K(x,z):
    x = x.flatten()
    z = z.flatten()
    if x.size != z.size:
        raise ValueError
    return math.exp(-0.5 * np.sum((x-z) ** 2))


# In[117]:


# Input: number of iterations L
# numpy matrix X of features, with n rows (samples), d columns (features)
# X[i,j] is the j-th feature of the i-th sample
# numpy vector y of labels, with n rows (samples), 1 column
# y[i] is the label (+1 or -1) of the i-th sample
# Output: numpy vector alpha of n rows, 1 column
def kerperceptron(L,X,y):
    n,d = X.shape
    alpha = np.zeros((n,1))
    
    reshaped = []
    for ele in X:
        reshaped.append(np.reshape(ele, (d,1)))
        
    for epochs in range(1, L+1):
        for t in range(n):
            #summation 
            summy = 0
            for i in range(n):
                summy += (alpha[i] * y[i] * K(reshaped[i],reshaped[t]))

            if y[t] * summy <= 0:
                alpha[t] += 1

    return alpha


# In[118]:


# Input: numpy vector alpha of n rows, 1 column
# numpy matrix X of features, with n rows (samples), d columns (features)
# X[i,j] is the j-th feature of the i-th sample
# numpy vector y of labels, with n rows (samples), 1 column
# y[i] is the label (+1 or -1) of the i-th sample
# numpy vector z of d rows, 1 column
# Output: label (+1 or -1)
def predictor(alpha,X,y,z):
# Your code goes here
    n,d = X.shape
    summy = 0

    for i in range(n):
        summy += alpha[i] * y[i] * K(np.reshape(X[i], (d,1)), z)

    if summy  > 0:
        label = 1
    else:
        label = -1

    return label


# In[119]:


def multi_class_kerperceptron(L,X,y,ld,path="",save=False):
    diff_labels = np.unique(y)
    alpha_list = []
    label_list = []
    for val in diff_labels:
        lly = [1 if l == val else -1 for l in y]
        label_list.append(lly)
        alpha = kerperceptron(L, X, lly)
        alpha_list.append(alpha)
        print("Finished Model for {}({})".format(val,ld[val]))
        if save == True:
            write_model_csv(path, alpha, np.array(lly),ld[val])
        
    return np.array(alpha_list),np.array(label_list)


# In[120]:


def multi_class_predictor(alpha_list,X,label_list,z):
    labels = len(label_list)
    for i in range(labels):
        if predictor(alpha_list[i],X,label_list[i],z) == 1:
            return i+1
    return -1


# In[124]:


def load_sample(path,window_size=2,file_type='jpg'):
    if file_type == "jpg":
        img = Image.open(path, 'r') 
    elif file_type == 'csv':
        img = read_from_csv(path)
        
    img = img_to_binary(img)
    img = feature_vector(img,window_size=window_size)
    
    return img
        
def flow_from_dir(path, thresh=150, window_size=2, max_len=50,accepted_labels=[]):
    #../Text_Reader_Data/TrainSet/
    data_list = []
    label_list = []
    label_dictionary = {}
    label = 1
    for sample_dir in os.listdir(path):
        if (sample_dir[0] != '.' and os.path.isdir(os.path.join(path,sample_dir))) and (len(accepted_labels) == 0 or (sample_dir in accepted_labels)):
            print("Reading from {}".format(sample_dir))
            label_dictionary[label] = sample_dir
            sample_data = read_from_dir(os.path.join(path,sample_dir),thresh=thresh,window_size=window_size,max_len=max_len)
            sample_labels = (label * np.ones(sample_data.shape[0])).reshape(-1,1)

            data_list.append(sample_data)
            label_list.append(sample_labels)
            label += 1
            
    np_data_list = data_list[0]
    for ele in range(1,len(data_list)):
        np_data_list = np.concatenate((np_data_list,data_list[ele]), axis=0)
        
    np_label_list = label_list[0]
    for ele in range(1,len(label_list)):
        np_label_list = np.concatenate((np_label_list,label_list[ele]), axis=0)
            
    print("--- COMPLETED READING IN DATA ---")
    return np_data_list,np_label_list,label_dictionary
                
def read_from_dir(path, thresh=150, window_size=2, max_len=50):
    data_set = []
    for file in os.listdir(path):
        if file[0] != '.':
            img = Image.open(os.path.join(path,file), 'r')
            img = img_to_binary(img, thresh)
            img = feature_vector(img,window_size)
            data_set.append(img)
            
        if len(data_set) > max_len:
            break
            
    return np.array(data_set)


# In[123]:


def write_model_csv(path,alpha, label, char_model):
    np.savetxt(os.path.join(path,"ALPHA_LIST_{}.csv".format(char_model)), alpha, delimiter=",")
    np.savetxt(os.path.join(path, "LABEL_LIST_{}.csv".format(char_model)), label, delimiter=",")
    
def write_data_csv(path,X,y,ld):
    np.savetxt(os.path.join(path, "DATA.csv"), X, delimiter=",")
    np.savetxt(os.path.join(path, "LABELS.csv"), y, delimiter=",")
    
    #vals = np.array([ord(val) for val in ld.values()])
    vals = []
    for val in ld.values():
        if len(val) > 1:
            if "-" in val:
                vals.append(ord(val[-1]))
            else:
                print("WARNING INCORRECT WRITING OF DICTIONARY")
        else:
            vals.append(ord(val))
    vals = np.array(vals)
    keys = np.array(list(ld.keys()))
    z = np.ones((2,vals.shape[0]))
    z[0] = vals
    z[1] = keys
    np.savetxt(os.path.join(path,"DICT.csv"), z, delimiter=",")


# In[122]:


def read_from_csv(path,thresh=150, window_size=2, accepted_labels=[],size=300):
    data_list = []
    label_list = []
    
    with open(path) as f:
        lis=[line.split() for line in f]
        for row in lis:
            str_list = row[0].split(',')
            if str_list[0] == 'label':
                continue
                
            for i in range(len(str_list)):
                str_list[i] = int(str_list[i])
                
            x = np.array(str_list)
            label = x[0]
            if len(accepted_labels) == 0 or (label in accepted_labels):
                x = x[1:].reshape((28,28))
                x = img_to_binary(x,thresh=thresh)
                x = feature_vector(x,window_size=window_size)

                data_list.append(x)
                label_list.append(label)
                
            if len(data_list) > size:
                break
    
    return np.array(data_list), np.array(label_list).reshape((-1,1))

def add_to_dict(ld,labels):
    new_labels = labels[:,:]
    max_key = max(ld.keys())
    max_key += 1
    unique_labels = np.unique(labels)
    for val in unique_labels:
        new_labels[new_labels == val] = max_key
        ld[max_key] = val
        max_key += 1
        
    return ld, new_labels

def merge_data(data1,label1,data2,label2,ld1):
    new_data = np.concatenate((data1,data2))
    new_dict, new_label2 = add_to_dict(ld1,label2)
    new_labels = np.concatenate((label1,new_label2))
    return new_data,new_labels,new_dict


# In[97]:


# X,y,ld = flow_from_dir('../Text_Reader_Data/TrainSet/', max_len=100, window_size=2)
#     # YOU CAN CHANGE THE PATH TO THE TRAIN DATA ABOVE
# write_data_csv("backup",X,y,ld)
#     # BACKUP IS THE NAME OF THE FILE THAT THE BACKUP DATA WILL BE STORED IN
# alpha_list,label_list = multi_class_kerperceptron(5,X,y,ld,path="backup",save=True)
# z = load_sample("../Text_Reader_Data/ValidationSet/small-a/50.jpg")
# ld[multi_class_predictor(alpha_list, X, label_list,z)]


# In[136]:


# num_data, num_labels = read_from_csv("../Text_Reader_Data/numbers.csv",accepted_labels=[2,3,4],size=300)
# X,y,ld = flow_from_dir('../Text_Reader_Data/TrainSet/', max_len=100, window_size=2, accepted_labels=["P","small-r","small-i","N","small-t"])
# ND,NL,NDi = merge_data(X,y,num_data,num_labels,ld)

# alpha_list,label_list = multi_class_kerperceptron(5,ND,NL,NDi)


# In[163]:


z = load_sample("../Text_Reader_Data/test/5")
NDi[multi_class_predictor(alpha_list, ND, label_list,z)]

