import cv2

# 定义参数
img_path = "cd_img.png"
path = "cd_result.png"
result_path = "cd_final.png"
need_background = False
target_alpha = 0.5
target_r = 255
target_g = 0
target_b = 0

# 读取图片
crop_image = cv2.imread(path) # 结果图
img = cv2.imread(img_path, cv2.IMREAD_UNCHANGED) # 原图

# 将二值图片白色部分变颜色
Conv_hsv_Gray = cv2.cvtColor(crop_image, cv2.COLOR_BGR2GRAY)
res, mask = cv2.threshold(Conv_hsv_Gray, 0, 255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)
# 0为需要保留的颜色（即黑色），利用掩膜思想覆盖白色部分
crop_image[mask == 0] = [target_b, target_g, target_r]

# 需要背景图
if need_background:
    # 将二值图片黑色部分变透明
    tmp = cv2.cvtColor(crop_image, cv2.COLOR_BGR2GRAY)
    _, alpha = cv2.threshold(tmp, 0, 255, cv2.THRESH_BINARY)
    # 修改图片透明度
    alpha = (alpha * target_alpha).astype("uint8")
    b, g, r = cv2.split(crop_image)
    rgba = [b, g, r, alpha]
    result = cv2.merge(rgba, 4)

    # 图片叠加
    alpha_result = result[:, :, 3] / 255.0
    alpha_img = 1 - alpha_result
    for c in range(0, 3):
        img[:, :, c] = ((alpha_img * img[:, :, c]) + (alpha_result * result[:, :, c]))

    # 图片保存
    cv2.imwrite(result_path, img)

# 不需要背景图
else:
    # 修改图片透明度
    b, g, r = cv2.split(crop_image)
    _, _, _, a = cv2.split(cv2.imread(path, cv2.IMREAD_UNCHANGED))
    a = (a * target_alpha).astype("uint8")
    a[a == 0] = 255
    result = cv2.merge((b, g, r, a))
    # 图片保存
    cv2.imwrite(result_path, result)

