<template>
  <div ref="playerContainer" style="width: 660px; height: 500px; border:1px solid red;"></div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue';
import Player from 'xgplayer';

const props = defineProps({
  url: {
    type: String,
    required: true
  },
  isLive: {
    type: Boolean,
    default: true
  }
});

const playerContainer = ref(null);
let player = null;

// 初始化播放器
function initPlayer(url) {
  if (!url) return;
  if (player) {
    player.destroy();
    player = null;
  }

  player = new Player({
    id: playerContainer.value,
    url,
    type: 'flv',
    isLive: props.isLive,
    autoplay: true,
    playsinline: true,
    controls: true,
    customType: {
      flv: function(video, url) {
        return window.flvjs ? window.flvjs.createPlayer({ type: 'flv', url, isLive: props.isLive }).init() : null;
      }
    }
  });
}

// 监听 URL 变化，动态切换视频
watch(() => props.url, (newUrl) => {
  if (newUrl) {
    initPlayer(newUrl);
  }
});

onMounted(() => {
  if (props.url) {
    initPlayer(props.url);
  }
});

onBeforeUnmount(() => {
  if (player) {
    player.destroy();
    player = null;
  }
});
</script>
