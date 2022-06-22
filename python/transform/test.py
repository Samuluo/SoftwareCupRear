import cv2
import sys

def transform(path, result_path, r, g, b):
    # 读取图片
    crop_image = cv2.imread(path)

    # 将二值图片白色部分变颜色
    Conv_hsv_Gray = cv2.cvtColor(crop_image, cv2.COLOR_BGR2GRAY)
    res, mask = cv2.threshold(Conv_hsv_Gray, 0, 255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)
    # 0为需要保留的颜色（即黑色），利用掩膜思想覆盖白色部分
    crop_image[mask == 0] = [b, g, r]

    # 将二值图片黑色部分变透明
    tmp = cv2.cvtColor(crop_image, cv2.COLOR_BGR2GRAY)
    _, alpha = cv2.threshold(tmp, 0, 255, cv2.THRESH_BINARY)
    b, g, r = cv2.split(crop_image)
    rgba = [b, g, r, alpha]
    dst = cv2.merge(rgba, 4)

    # 注意保存成png格式！！！jpg的话还是黑色背景(255)
    cv2.imwrite(result_path, dst)


if __name__=='__main__':
    transform(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5])