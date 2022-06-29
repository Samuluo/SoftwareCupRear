import cv2
import numpy as np
from skimage.io import imsave, imread

def get_lut():
    lut = np.zeros((256,3), dtype=np.uint8)
    lut[0] = [255, 0, 0]
    lut[1] = [30, 255, 142]
    lut[2] = [60, 0, 255]
    lut[3] = [255, 222, 0]
    lut[4] = [0, 0, 0]
    return lut


im = "result.png"
im = cv2.imread(im, cv2.IMREAD_COLOR)
# cv2.bitwise_not(im, im)
# cv2.imwrite("result.png", im)

if im.ndim == 3:
    im = cv2.cvtColor(im, cv2.COLOR_BGR2GRAY)
    im = get_lut()[im]
imsave("test.png", im)