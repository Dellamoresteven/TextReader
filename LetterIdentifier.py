import numpy as np 
#THIS IS STEVENS CODE BACK OFF U LOZER
# This will do things my dude
IMG_SIZE = 150
TRAIN_DIR = 'PATH TO TRAIN DIR'
VALIDATE_DIR = 'PATH TO VALIDATE DIR'

def create_model():
	from keras.models import Sequential
	from keras.layers import Conv2D, MaxPooling2D
	from keras.layers import Activation, Dropout, Flatten, Dense

	model = Sequential()
	model.add(Conv2D(32, (3, 3), input_shape=(IMG_SIZE, IMG_SIZE, 3)))
	model.add(Activation('relu'))
	model.add(MaxPooling2D(pool_size=(3, 3)))

	model.add(Conv2D(64, (3, 3)))
	model.add(Activation('relu'))
	model.add(MaxPooling2D(pool_size=(3, 3)))

	model.add(Conv2D(32, (3, 3)))
	model.add(Activation('relu'))
	model.add(MaxPooling2D(pool_size=(3, 3)))

	model.add(Flatten())  # this converts our 3D feature maps to 1D feature vectors

	model.add(Dense(64))
	model.add(Activation('relu'))

	model.add(Dropout(0.25))

	model.add(Dense(1))

	model.compile(loss='binary_crossentropy',
	              optimizer='rmsprop',
	              metrics=['accuracy'])

	return model

def train_model(model, batch_size=100,epoch=5):
	from keras.preprocessing.image import ImageDataGenerator

	batch_size = 16

	# this is the augmentation configuration we will use for training
	train_datagen = ImageDataGenerator(
	        rescale=1./255,
	        shear_range=0.2,
	        zoom_range=0.2,
	        horizontal_flip=True,
	        rotate=0.9,
	        )

	# this is the augmentation configuration we will use for testing:
	# only rescaling
	validate_datagen = ImageDataGenerator(
	    rescale=1./255,
	    )

	# this is a generator that will read pictures found in
	# subfolers of 'data/train', and indefinitely generate
	# batches of augmented image data
	train_generator = train_datagen.flow_from_directory(
	        TRAIN_DIR,  # this is the target directory
	        target_size=(IMG_SIZE, IMG_SIZE),  # all images will be resized to 150x150
	        #color_mode='grayscale', # sets the images to grayscale
	        batch_size=batch_size,
	        class_mode='categorical', # since we use binary_crossentropy loss, we need binary labels
	        )  

	# this is a similar generator, for validation data
	validation_generator = validate_datagen.flow_from_directory(
	        VALIDATE_DIR,
	        target_size=(IMG_SIZE, IMG_SIZE),
	        #color_mode='grayscale',
	        batch_size=batch_size,
	        class_mode='categorical',
	        )

	model.fit_generator(
        train_generator,
        steps_per_epoch=batch_size // batch_size,
        epochs=epoch,
        validation_data=validation_generator,
        validation_steps=batch_size // batch_size
        )

	return model