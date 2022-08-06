import paddlers as pdrs
from skimage.io import imsave

cd_predictor = pdrs.deploy.Predictor('model/changeDetection', use_gpu=True)

while True:

    # file1 = "G:\\BaiduNetdiskDownload\\CD_Data_GZ\\CD_Data_GZ\\T1\\P_GZ_test4_2010_1107_Level_18.tif"
    file1 = "E:\\Building change detection dataset_2\\Building change detection dataset\\1. The two-period image data\\after\\after.tif"

    # file2 = "G:\\BaiduNetdiskDownload\\CD_Data_GZ\\CD_Data_GZ\\T2\\P_GZ_test4_2019_0615_Level_18.tif"
    file2 = "E:\\Building change detection dataset_2\\Building change detection dataset\\1. The two-period image data\\before\\before.tif"

    file1 = input("file1:")
    file2 = input("file2:")

    result_path = "result.png"
    # 开始预测
    result = cd_predictor.predict(img_file=(file1, file2))
    result = result['label_map']
    # 保存结果
    imsave(result_path + ".png", result, check_contrast=False)