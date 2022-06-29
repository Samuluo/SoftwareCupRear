import paddle
import paddlers as pdrs
from skimage.io import imsave
import cv2

# file_path = "img.png"
# save_path = "result.png"
file_path = "../example/oe/uploads/"
save_path = "../example/oe/results/"

# 将导出模型所在目录传入Predictor的构造方法中
predictor = pdrs.deploy.Predictor('../../model/objectExtraction', use_gpu=True)
print(paddle.device.get_device())
# img_file参数指定输入图像路径
for i in range(1, 11):
    result = predictor.predict(img_file=file_path + str(i) + ".png")
    result = result['label_map']
    imsave(save_path + str(i) + ".png", result, check_contrast=False)

# # 将二值图片黑色部分变透明
# im = cv2.imread(save_path)
# tmp = cv2.cvtColor(im, cv2.COLOR_BGR2GRAY)
# _, alpha = cv2.threshold(tmp, 0, 255, cv2.THRESH_BINARY)
# b, g, r = cv2.split(im)
# rgba = [b, g, r, alpha]
# im = cv2.merge(rgba, 4)
# cv2.imwrite(save_path, im)