<template>
  <BContainer>
    <h4 class="text-muted mb-0 fw-semibold">파일 확장자 차단</h4>
    <hr class="mb-3"/>
    <BRow class="mb-3">
      <BCol class="col-3 text-muted">
        <strong>고정 확장자</strong>
      </BCol>
      <BCol class="col-9 justify-content-between">
        <BCardGroup deck class="justify-content-start">
          <BFormCheckbox
              class="m-0"
              v-for="(fixExtension) in fixedExtensionOptions"
              :key="fixExtension.id"
              value="true"
              unchecked-value="false"
              v-model="arr[fixExtension.id]"
              @click="updateFixedExtension(fixExtension.id)"
          > {{ fixExtension.title }}
          </BFormCheckbox>
        </BCardGroup>
      </BCol>
    </BRow>
    <BRow>
      <BCol class="col-3 text-muted">
        <strong>커스텀 확장자</strong>
      </BCol>
      <BCol class="col-9">
        <BInputGroup class="mb-2 d-flex">
          <BFormInput
              size="sm"
              v-model="inputValue"
              @keydown.enter="onClickAdd()"
              placeholder="최대 입력 길이 20자"
              class="form-control"
          />
          <BButton
              @click="onClickAdd()"
              size="sm"
              variant="outline-secondary"
          >
            추가
          </BButton>
        </BInputGroup>
          <small class="m-1 text-secondary">{{ customExtensionCount }} / 200 </small>
          <BCardGroup deck class="border border-secondary-subtle rounded-3 p-1 mt-1">
          <div>
            <div
                v-for="extension in customExtensions"
                :key="extension"
                class="bg-light text-secondary text-body-emphasis d-inline-flex border border-secondary-subtle rounded-3 ps-2 p-1 m-1 small justify-content-evenly align-items-center"
            >
              <small class="pe-1"> {{ extension.title }} </small>
              <BButton
                  type="button"
                  class="btn-close"
                  aria-label="Close"
                  style="font-size: xx-small;"
                  @click="onClickRemove(e, extension)"
              />
            </div>
         </div>
        </BCardGroup>
      </BCol>
<!--    <BRow class="m-0" style="text-align: end">-->
<!--      <BCol>-->
<!--        <BBadge-->
<!--            @click="reset()"-->
<!--            class="me-1"-->
<!--            size="sm"-->
<!--            variant="secondary"-->
<!--            pill>-->
<!--          초기화-->
<!--        </BBadge>-->
<!--      </BCol>-->
<!--    </BRow>-->
    </BRow>
  </BContainer>
</template>

<script setup>
import {computed, onMounted, onBeforeMount, ref, watch} from 'vue';
import axios from "axios";
import {useCookies} from "vue3-cookies";

// init
const { cookies } = useCookies();
const TOKEN_KEY = "blocking-extension-token";
const token = ref("");
const fixedExtensionOptions = ref([]);
const fixedExtensionSelected = ref([]);
const savedExtensions = ref([]);
const customExtensions = ref([]);
const inputValue = ref("");
const customExtensionCount = ref(0);
const arr = ref({});

onBeforeMount(() => {
  getFixedExtension();
  getUserExtension();
})

/**
 *
 * @description 고정 확장자 조회 API
 * @returns {Promise<void>}
 */
const getFixedExtension = async () => {
  try {
    await axios.get(`/api/v1/blocking-extensions?type=FIXED`)
        .then(res => {
          fixedExtensionOptions.value = res.data;
        })
  } catch (error) {
    console.log(error);
  }
};

/**
 * @description 유저 저장 확장자 조회 API
 * @returns {Promise<void>}
 */
const getUserExtension = async () => {
  if(token.value == null || token.value.length <= 0) {
    await setToken();
  }

  try {
    await axios.get(`/api/v1/blocking-extensions/user?token=${token.value}`)
        .then(res => {
          savedExtensions.value = res.data;
        }).then(res => {
          setExtension();
        })
  } catch (error) {
    console.log(error);
  }
}

/**
 * @description 쿠키 setter
 * @returns {UnwrapRef<string>}
 */
const setToken = async () => {
    token.value = cookies.get(TOKEN_KEY);
}

/**
 * @description 쿠키 getter
 * @returns {UnwrapRef<string>}
 */
const getToken = () => {
  return token.value;
}

/**
 * @description 커스텀 확장자 삭제 API 요청
 * @param v
 * @returns {Promise<void>}
 */
const deleteUserExtension = async (v) => {
  const data = JSON.stringify({
    "token": token.value,
    "extensionId": v.id
  })

  try {
    await axios.put(`/api/v1/blocking-extensions/user`,
        data,
        {
          headers: {'Content-Type': "application/json"}
        }
    ).then((res) =>{
      savedExtensions.value = res.data;
      setExtension();
    })
  } catch (error) {
    errorHandler(error);
    await getUserExtension();
  }
}

/**
 * @description 다건 고정 확정자 update(저장/삭제) API
 * @returns {Promise<void>}
 */
const updateFixedExtensions = async () => {

  const extensionIds = fixedExtensionSelected.value.map(i => i.id ? i.id : i);
  console.log(extensionIds);

  const data = JSON.stringify({
    "token": token.value,
    "extensionIds": extensionIds
  })

  try {
    await axios.post(`/api/v1/blocking-extensions/user/multiple`,
        data,
        {
          headers: {'Content-Type': "application/json"}
        }
    ).then((res) =>{
          savedExtensions.value = res.data;
          setExtension();
        }
    )
  } catch (error) {
    errorHandler(error);
    await getUserExtension();
  }
}

/**
 * @description 단건 고정 확정자 update(저장/삭제) API
 * @returns {Promise<void>}
 */
const updateFixedExtension = async (v) => {
  const data = JSON.stringify({
    "token": token.value,
    "extensionId": v
  })

  try {
    await axios.post(`/api/v1/blocking-extensions/user/fixed`,
        data,
        {
          headers: {'Content-Type': "application/json"}
        }
    ).then((res) =>{
          savedExtensions.value = res.data;
          setExtension();
        }
    )
  } catch (error) {
    errorHandler(error);
    await getUserExtension();
  }
}

/**
 * @description 커스텀 확장자 등록 API 요청
 * @param v
 * @returns {Promise<void>}
 */
const updateUserExtension = async (v) => {
    const data = JSON.stringify({
      "token": token.value,
      "extensionTitle": v
    });

    try {
      await axios.post(`/api/v1/blocking-extensions/user/custom`,
          data,
          {
            headers: {'Content-Type': "application/json"}
          }
      ).then((res) =>{
            savedExtensions.value = res.data;
            setExtension();
          }
      )
    } catch (error) {
      errorHandler(error);
      await getUserExtension();
    }
}

/**
 * @description 유저 확장자 화면 setting
 */
const setExtension = () => {
  for(let i=0; i<arr.value.length; i++) {
    arr.value[i] = "false";
    console.log(arr.value[i])
  }

  fixedExtensionSelected.value = savedExtensions.value.filter(item => item.extensionType === 'FIXED')
  console.log(fixedExtensionSelected.value)
  fixedExtensionSelected.value.forEach(e => {
    console.log(arr.value[e.id]);
    arr.value[e.id] = "true";
  });

  customExtensions.value = savedExtensions.value.filter(item => item.extensionType === 'CUSTOM');
  customExtensionCount.value = customExtensions.value.length;
}

/**
 * @description 추가 버튼 클릭 이벤트
 * @returns {Promise<void>}
 */
const onClickAdd = async () => {

  inputValue.value = inputValue.value.trim();

  if(inputValue.value.length > 20) {
    alert("최대 입력 길이는 20자 입니다.");
    return;
  }

  if(inputValue.value.length == 0) {
    alert("내용을 입력하세요");
    return;
  }

  // 고정 확장자인 경우 확인
  let dupled = fixedExtensionOptions.value.filter(fixed => fixed.title == inputValue.value);
  if(dupled.length > 0) {
    if(savedExtensions.value.filter(saved => saved.title == inputValue.value) <= 0) {
      await updateFixedExtension(dupled[0].id);
    }
    inputValue.value = "";
  } else {
    if (customExtensions.value.length >= 200) {
      alert('커스텀 확장자는 200개 이상 저장할 수 없습니다.');
      return;
    }

    await updateUserExtension(inputValue.value)
        .then(res => {
          inputValue.value = "";
        })
  }
}

/**
 * @description 삭제 버튼 클릭 이벤트
 * @param e
 * @param v
 * @returns {Promise<void>}
 */
const onClickRemove = async (e, v) => {
  await deleteUserExtension(v);
}

/**
 * @description reset 이벤트
 * @returns {Promise<void>}
 */
const reset = async () => {
  if(token.value == null || token.value.length <= 0) {
    await setToken();
  }

  try {
    await axios.get(`/api/v1/blocking-extensions/reset?token=${token.value}`)
      .then((res) =>{
          savedExtensions.value = res.data;
      }).then(res => setExtension())
  } catch (error) {
    errorHandler(error);
    await getUserExtension();
  }
}

/**
 * @default 에러 핸들러
 * @param error
 */
const errorHandler = (error) => {
  console.log(error.response.data.code)
  if(error.response.data.code.startsWith("E")) {
    alert(error.response.data.message);
  } else {
    alert(error);
  }
}

</script>
