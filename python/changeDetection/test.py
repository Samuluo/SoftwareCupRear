import paddle
import paddlers as pdrs
from skimage.io import imsave
import sys

def changeDetection(path1, path2, result_path):
    # 将导出模型所在目录传入Predictor的构造方法中
    predictor = pdrs.deploy.Predictor('model/changeDetection', use_gpu=True)
    # print(paddle.device.get_device())
    # img_file参数指定输入图像路径
    result = predictor.predict(img_file=(path1, path2))
    result = result[0]['label_map']
    # print(result)
    imsave(result_path, result, check_contrast=False)
    return result_path

if __name__=='__main__':
    changeDetection(sys.argv[1] ,sys.argv[2] ,sys.argv[3])
