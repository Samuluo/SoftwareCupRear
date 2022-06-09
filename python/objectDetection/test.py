import paddle
import paddlers as pdrs
from skimage.io import imsave


file_path = "img.jpg"
save_path = "./result.jpg"

# 将导出模型所在目录传入Predictor的构造方法中
predictor = pdrs.deploy.Predictor('model/objectDetection', use_gpu=True)
print(paddle.device.get_device())
# img_file参数指定输入图像路径
result = predictor.predict(img_file=file_path)
print(result[0])

# imsave(save_path, result, check_contrast=False)
