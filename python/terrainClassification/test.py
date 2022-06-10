import paddle
import paddlers as pdrs
from skimage.io import imsave
import sys

def terrainClassification(path, result_path):
    print(result_path)
    # 将导出模型所在目录传入Predictor的构造方法中
    predictor = pdrs.deploy.Predictor('model/terrainClassification', use_gpu=True)
#     print(paddle.device.get_device())
    # img_file参数指定输入图像路径
    result = predictor.predict(img_file=path)
#     print(result)
    result = result['label_map']
    imsave(result_path, result, check_contrast=False)

if __name__=='__main__':
    terrainClassification(sys.argv[1], sys.argv[2])
