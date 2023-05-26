<script lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

export default {
  props: {
    postId: {
      type: [Number, String],
      required: true,
    },
  },
  setup(props) {
    const post = ref({
      id: 0,
      title: "",
      content: "",
    });

    onMounted(() => {
      axios.get(`/api/posts/${props.postId}`).then((response) => {
        post.value = response.data;
      });
    });

    const router = useRouter();

    const moveToEdit = () => {
      router.push({name : "edit", params: { postId: props.postId } });
    }

    return {
      post, moveToEdit
    };
  },
};

</script>

<template>
  <h2>{{post.title}}</h2>
  <div>{{post.content}}</div>

  <el-button type="warning" @click="moveToEdit()">수정</el-button>
</template>